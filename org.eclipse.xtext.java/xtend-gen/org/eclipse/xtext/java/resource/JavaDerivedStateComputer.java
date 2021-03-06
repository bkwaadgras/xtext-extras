package org.eclipse.xtext.java.resource;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.binary.BinaryClass;
import org.eclipse.xtext.common.types.access.binary.asm.ClassFileBytesAccess;
import org.eclipse.xtext.common.types.access.binary.asm.JvmDeclaredTypeBuilder;
import org.eclipse.xtext.common.types.descriptions.EObjectDescriptionBasedStubGenerator;
import org.eclipse.xtext.java.resource.ClassFileCache;
import org.eclipse.xtext.java.resource.InMemoryClassLoader;
import org.eclipse.xtext.java.resource.IndexAwareNameEnvironment;
import org.eclipse.xtext.java.resource.JavaConfig;
import org.eclipse.xtext.java.resource.JavaResource;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.parser.antlr.IReferableElementsUnloader;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.JavaVersion;
import org.eclipse.xtext.util.internal.Log;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@Log
@SuppressWarnings("all")
public class JavaDerivedStateComputer {
  @Inject
  private IReferableElementsUnloader unloader;
  
  @Inject
  private EObjectDescriptionBasedStubGenerator stubGenerator;
  
  @Inject
  private IResourceDescriptionsProvider resourceDescriptionsProvider;
  
  public void discardDerivedState(final Resource resource) {
    EList<EObject> resourcesContentsList = resource.getContents();
    for (final EObject eObject : resourcesContentsList) {
      this.unloader.unloadRoot(eObject);
    }
    resource.getContents().clear();
    boolean _isInfoFile = this.isInfoFile(resource);
    if (_isInfoFile) {
      return;
    }
    CompilationUnit _compilationUnit = this.getCompilationUnit(resource);
    boolean _tripleNotEquals = (_compilationUnit != null);
    if (_tripleNotEquals) {
      final ClassFileCache classFileCache = this.findOrCreateClassFileCache(resource.getResourceSet());
      classFileCache.addResourceToCompile(resource);
    }
  }
  
  public void installStubs(final Resource resource) {
    boolean _isInfoFile = this.isInfoFile(resource);
    if (_isInfoFile) {
      return;
    }
    final CompilationUnit compilationUnit = this.getCompilationUnit(resource);
    IErrorHandlingPolicy _proceedWithAllProblems = DefaultErrorHandlingPolicies.proceedWithAllProblems();
    CompilerOptions _compilerOptions = this.getCompilerOptions(resource);
    DefaultProblemFactory _defaultProblemFactory = new DefaultProblemFactory();
    ProblemReporter _problemReporter = new ProblemReporter(_proceedWithAllProblems, _compilerOptions, _defaultProblemFactory);
    final Parser parser = new Parser(_problemReporter, true);
    final CompilationResult compilationResult = new CompilationResult(compilationUnit, 0, 1, (-1));
    final CompilationUnitDeclaration result = parser.dietParse(compilationUnit, compilationResult);
    if ((result.types != null)) {
      for (final TypeDeclaration type : result.types) {
        {
          ImportReference _currentPackage = result.currentPackage;
          char[][] _importName = null;
          if (_currentPackage!=null) {
            _importName=_currentPackage.getImportName();
          }
          List<String> _map = null;
          if (((List<char[]>)Conversions.doWrapArray(_importName))!=null) {
            final Function1<char[], String> _function = (char[] it) -> {
              return String.valueOf(it);
            };
            _map=ListExtensions.<char[], String>map(((List<char[]>)Conversions.doWrapArray(_importName)), _function);
          }
          String _join = null;
          if (_map!=null) {
            _join=IterableExtensions.join(_map, ".");
          }
          final String packageName = _join;
          final JvmDeclaredType jvmType = this.createType(type, packageName);
          resource.getContents().add(jvmType);
        }
      }
    }
  }
  
  public JvmDeclaredType createType(final TypeDeclaration type, final String packageName) {
    JvmDeclaredType _switchResult = null;
    int _kind = TypeDeclaration.kind(type.modifiers);
    switch (_kind) {
      case TypeDeclaration.CLASS_DECL:
        _switchResult = TypesFactory.eINSTANCE.createJvmGenericType();
        break;
      case TypeDeclaration.INTERFACE_DECL:
        JvmGenericType _createJvmGenericType = TypesFactory.eINSTANCE.createJvmGenericType();
        final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
          it.setInterface(true);
        };
        _switchResult = ObjectExtensions.<JvmGenericType>operator_doubleArrow(_createJvmGenericType, _function);
        break;
      case TypeDeclaration.ENUM_DECL:
        _switchResult = TypesFactory.eINSTANCE.createJvmEnumerationType();
        break;
      case TypeDeclaration.ANNOTATION_TYPE_DECL:
        _switchResult = TypesFactory.eINSTANCE.createJvmAnnotationType();
        break;
      default:
        String _string = type.toString();
        String _plus = ("Cannot handle type " + _string);
        throw new IllegalArgumentException(_plus);
    }
    final JvmDeclaredType jvmType = _switchResult;
    jvmType.setPackageName(packageName);
    jvmType.setSimpleName(String.valueOf(type.name));
    if ((jvmType instanceof JvmGenericType)) {
      if ((type.typeParameters != null)) {
        for (final TypeParameter typeParam : type.typeParameters) {
          {
            final JvmTypeParameter jvmTypeParam = TypesFactory.eINSTANCE.createJvmTypeParameter();
            jvmTypeParam.setName(String.valueOf(typeParam.name));
            EList<JvmTypeParameter> _typeParameters = ((JvmGenericType)jvmType).getTypeParameters();
            _typeParameters.add(jvmTypeParam);
          }
        }
      }
    }
    if ((type.memberTypes != null)) {
      for (final TypeDeclaration nestedType : type.memberTypes) {
        {
          final JvmDeclaredType nested = this.createType(nestedType, null);
          EList<JvmMember> _members = jvmType.getMembers();
          _members.add(nested);
        }
      }
    }
    return jvmType;
  }
  
  public CompilationUnit getCompilationUnit(final Resource resource) {
    return ((JavaResource) resource).getCompilationUnit();
  }
  
  protected ClassFileCache findOrCreateClassFileCache(final ResourceSet rs) {
    ClassFileCache cache = ClassFileCache.findInEmfObject(rs);
    if ((cache == null)) {
      ClassFileCache _classFileCache = new ClassFileCache();
      cache = _classFileCache;
      cache.attachToEmfObject(rs);
    }
    return cache;
  }
  
  public void installFull(final Resource resource) {
    boolean _isInfoFile = this.isInfoFile(resource);
    if (_isInfoFile) {
      return;
    }
    final ClassFileCache classFileCache = this.findOrCreateClassFileCache(resource.getResourceSet());
    final CompilationUnit compilationUnit = this.getCompilationUnit(resource);
    final ClassLoader classLoader = this.getClassLoader(resource);
    final IResourceDescriptions data = this.resourceDescriptionsProvider.getResourceDescriptions(resource.getResourceSet());
    if ((data == null)) {
      throw new IllegalStateException("No index installed");
    }
    final Procedure2<List<String>, Map<String, byte[]>> _function = (List<String> topLevelTypes, Map<String, byte[]> classMap) -> {
      final InMemoryClassLoader inMemClassLoader = new InMemoryClassLoader(classMap, classLoader);
      for (final String topLevel : topLevelTypes) {
        try {
          BinaryClass _binaryClass = new BinaryClass(topLevel, inMemClassLoader);
          ClassFileBytesAccess _classFileBytesAccess = new ClassFileBytesAccess();
          final JvmDeclaredTypeBuilder builder = new JvmDeclaredTypeBuilder(_binaryClass, _classFileBytesAccess, inMemClassLoader);
          final JvmDeclaredType type = builder.buildType();
          EList<EObject> _contents = resource.getContents();
          _contents.add(type);
        } catch (final Throwable _t) {
          if (_t instanceof Throwable) {
            final Throwable t = (Throwable)_t;
            throw new IllegalStateException((("Could not load type \'" + topLevel) + "\'"), t);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    };
    final Procedure2<? super List<String>, ? super Map<String, byte[]>> initializer = _function;
    final boolean wasCached = classFileCache.popCompileResult(compilationUnit.fileName, initializer);
    if ((!wasCached)) {
      final Function1<Resource, CompilationUnit> _function_1 = (Resource it) -> {
        return this.getCompilationUnit(it);
      };
      List<CompilationUnit> _list = IterableExtensions.<CompilationUnit>toList(IterableExtensions.<Resource, CompilationUnit>map(classFileCache.drainResourcesToCompile(), _function_1));
      final HashSet<CompilationUnit> unitsToCompile = new HashSet<CompilationUnit>(_list);
      unitsToCompile.add(compilationUnit);
      final IndexAwareNameEnvironment nameEnv = new IndexAwareNameEnvironment(resource, classLoader, data, this.stubGenerator, classFileCache);
      IErrorHandlingPolicy _proceedWithAllProblems = DefaultErrorHandlingPolicies.proceedWithAllProblems();
      CompilerOptions _compilerOptions = this.getCompilerOptions(resource);
      final ICompilerRequestor _function_2 = (CompilationResult it) -> {
        ClassFile[] _classFiles = it.getClassFiles();
        for (final ClassFile cls : _classFiles) {
          {
            final QualifiedName key = QualifiedName.create(CharOperation.toStrings(cls.getCompoundName()));
            final Function<QualifiedName, IBinaryType> _function_3 = (QualifiedName name) -> {
              try {
                byte[] _bytes = cls.getBytes();
                char[] _fileName = cls.fileName();
                return new ClassFileReader(_bytes, _fileName);
              } catch (Throwable _e) {
                throw Exceptions.sneakyThrow(_e);
              }
            };
            classFileCache.computeIfAbsent(key, _function_3);
          }
        }
        final HashMap<String, byte[]> map = CollectionLiterals.<String, byte[]>newHashMap();
        List<String> topLevelTypes = CollectionLiterals.<String>newArrayList();
        ClassFile[] _classFiles_1 = it.getClassFiles();
        for (final ClassFile cf : _classFiles_1) {
          {
            final String className = CharOperation.toString(cf.getCompoundName());
            map.put(className, cf.getBytes());
            if ((!cf.isNestedType)) {
              topLevelTypes.add(className);
            }
          }
        }
        boolean _equals = Arrays.equals(it.fileName, compilationUnit.fileName);
        if (_equals) {
          initializer.apply(topLevelTypes, map);
        } else {
          classFileCache.addCompileResult(it.fileName, topLevelTypes, map);
        }
      };
      DefaultProblemFactory _defaultProblemFactory = new DefaultProblemFactory();
      final org.eclipse.jdt.internal.compiler.Compiler compiler = new org.eclipse.jdt.internal.compiler.Compiler(nameEnv, _proceedWithAllProblems, _compilerOptions, _function_2, _defaultProblemFactory);
      compiler.compile(((ICompilationUnit[])Conversions.unwrapArray(unitsToCompile, ICompilationUnit.class)));
    }
  }
  
  protected boolean isInfoFile(final Resource resource) {
    final String name = resource.getURI().trimFileExtension().lastSegment();
    return (Objects.equal(name, "package-info") || Objects.equal(name, "module-info"));
  }
  
  protected CompilerOptions getCompilerOptions() {
    return this.getCompilerOptions(((JavaConfig) null));
  }
  
  protected CompilerOptions getCompilerOptions(final Resource resource) {
    ResourceSet _resourceSet = null;
    if (resource!=null) {
      _resourceSet=resource.getResourceSet();
    }
    CompilerOptions _compilerOptions = null;
    if (_resourceSet!=null) {
      _compilerOptions=this.getCompilerOptions(_resourceSet);
    }
    return _compilerOptions;
  }
  
  protected CompilerOptions getCompilerOptions(final ResourceSet resourceSet) {
    return this.getCompilerOptions(JavaConfig.findInEmfObject(resourceSet));
  }
  
  protected CompilerOptions getCompilerOptions(final JavaConfig javaConfig) {
    JavaVersion _elvis = null;
    JavaVersion _javaSourceLevel = null;
    if (javaConfig!=null) {
      _javaSourceLevel=javaConfig.getJavaSourceLevel();
    }
    if (_javaSourceLevel != null) {
      _elvis = _javaSourceLevel;
    } else {
      _elvis = JavaVersion.JAVA8;
    }
    final JavaVersion sourceVersion = _elvis;
    JavaVersion _elvis_1 = null;
    JavaVersion _javaTargetLevel = null;
    if (javaConfig!=null) {
      _javaTargetLevel=javaConfig.getJavaTargetLevel();
    }
    if (_javaTargetLevel != null) {
      _elvis_1 = _javaTargetLevel;
    } else {
      _elvis_1 = JavaVersion.JAVA8;
    }
    final JavaVersion targetVersion = _elvis_1;
    boolean _equals = Objects.equal(sourceVersion, JavaVersion.JAVA7);
    if (_equals) {
      JavaDerivedStateComputer.LOG.warn(
        "The java source language has been configured with Java 7. JDT will not produce signature information for generic @Override methods in this version, which might lead to follow up issues.");
    }
    final long sourceLevel = this.toJdtVersion(sourceVersion);
    final long targetLevel = this.toJdtVersion(targetVersion);
    final CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.targetJDK = targetLevel;
    compilerOptions.inlineJsrBytecode = true;
    compilerOptions.sourceLevel = sourceLevel;
    compilerOptions.produceMethodParameters = true;
    compilerOptions.produceReferenceInfo = true;
    compilerOptions.originalSourceLevel = targetLevel;
    compilerOptions.complianceLevel = sourceLevel;
    compilerOptions.originalComplianceLevel = targetLevel;
    return compilerOptions;
  }
  
  protected long toJdtVersion(final JavaVersion version) {
    return version.toJdtClassFileConstant();
  }
  
  protected ClassLoader getClassLoader(final Resource it) {
    ResourceSet _resourceSet = it.getResourceSet();
    Object _classpathURIContext = ((XtextResourceSet) _resourceSet).getClasspathURIContext();
    return ((ClassLoader) _classpathURIContext);
  }
  
  private static final Logger LOG = Logger.getLogger(JavaDerivedStateComputer.class);
}
