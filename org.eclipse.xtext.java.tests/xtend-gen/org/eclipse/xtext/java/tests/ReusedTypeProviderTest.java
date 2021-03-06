package org.eclipse.xtext.java.tests;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.build.BuildRequest;
import org.eclipse.xtext.build.IncrementalBuilder;
import org.eclipse.xtext.build.IndexState;
import org.eclipse.xtext.build.Source2GeneratedMapping;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.access.impl.AbstractTypeProviderTest;
import org.eclipse.xtext.common.types.testSetups.AbstractMethods;
import org.eclipse.xtext.common.types.testSetups.Bug347739ThreeTypeParamsSuperSuper;
import org.eclipse.xtext.common.types.testSetups.ClassWithVarArgs;
import org.eclipse.xtext.java.tests.JavaInjectorProvider;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ChunkedResourceDescriptions;
import org.eclipse.xtext.resource.impl.ProjectDescription;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(JavaInjectorProvider.class)
@SuppressWarnings("all")
public class ReusedTypeProviderTest extends AbstractTypeProviderTest {
  @Inject
  private IncrementalBuilder builder;
  
  @Inject
  private IResourceServiceProvider.Registry resourceServiceProviderRegistry;
  
  @Inject
  private IJvmTypeProvider.Factory typeProviderFactory;
  
  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;
  
  private static IJvmTypeProvider typeProvider;
  
  public static List<String> readResource(final String name) throws Exception {
    final InputStream stream = ReusedTypeProviderTest.class.getResourceAsStream(name);
    InputStreamReader _inputStreamReader = new InputStreamReader(stream);
    final BufferedReader reader = new BufferedReader(_inputStreamReader);
    try {
      String line = null;
      final List<String> result = Lists.<String>newArrayList();
      while (((line = reader.readLine()) != null)) {
        result.add(line);
      }
      return result;
    } finally {
      reader.close();
    }
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    if ((ReusedTypeProviderTest.typeProvider == null)) {
      final String pathToSources = "/org/eclipse/xtext/common/types/testSetups";
      final List<String> files = ReusedTypeProviderTest.readResource((pathToSources + "/files.list"));
      Set<IResourceDescription> _emptySet = CollectionLiterals.<IResourceDescription>emptySet();
      final ResourceDescriptionsData part = new ResourceDescriptionsData(_emptySet);
      XtextResourceSet _get = this.resourceSetProvider.get();
      final Procedure1<XtextResourceSet> _function = (XtextResourceSet it) -> {
        ProjectDescription _projectDescription = new ProjectDescription();
        final Procedure1<ProjectDescription> _function_1 = (ProjectDescription it_1) -> {
          it_1.setName("my-test-project");
        };
        final ProjectDescription projectDesc = ObjectExtensions.<ProjectDescription>operator_doubleArrow(_projectDescription, _function_1);
        projectDesc.attachToEmfObject(it);
        Map<String, ResourceDescriptionsData> _emptyMap = CollectionLiterals.<String, ResourceDescriptionsData>emptyMap();
        final ChunkedResourceDescriptions index = new ChunkedResourceDescriptions(_emptyMap, it);
        index.setContainer(projectDesc.getName(), part);
        it.setClasspathURIContext(ReusedTypeProviderTest.class.getClassLoader());
      };
      final XtextResourceSet resourceSet = ObjectExtensions.<XtextResourceSet>operator_doubleArrow(_get, _function);
      this.typeProviderFactory.createTypeProvider(resourceSet);
      BuildRequest _buildRequest = new BuildRequest();
      final Procedure1<BuildRequest> _function_1 = (BuildRequest it) -> {
        Iterable<String> _filterNull = IterableExtensions.<String>filterNull(files);
        for (final String file : _filterNull) {
          {
            final String fullPath = ((pathToSources + "/") + file);
            final URL url = ReusedTypeProviderTest.class.getResource(fullPath);
            List<URI> _dirtyFiles = it.getDirtyFiles();
            URI _createURI = URI.createURI(url.toExternalForm());
            _dirtyFiles.add(_createURI);
          }
        }
        it.setResourceSet(resourceSet);
        Source2GeneratedMapping _source2GeneratedMapping = new Source2GeneratedMapping();
        IndexState _indexState = new IndexState(part, _source2GeneratedMapping);
        it.setState(_indexState);
      };
      final BuildRequest buildRequest = ObjectExtensions.<BuildRequest>operator_doubleArrow(_buildRequest, _function_1);
      final Function1<URI, IResourceServiceProvider> _function_2 = (URI it) -> {
        return this.resourceServiceProviderRegistry.getResourceServiceProvider(it);
      };
      this.builder.build(buildRequest, _function_2);
      ReusedTypeProviderTest.typeProvider = this.typeProviderFactory.findTypeProvider(resourceSet);
    }
  }
  
  @Override
  protected IJvmTypeProvider getTypeProvider() {
    return ReusedTypeProviderTest.typeProvider;
  }
  
  @Override
  protected String getCollectionParamName() {
    return "arg0";
  }
  
  @Test
  @Override
  public void testFindTypeByName_AbstractMultimap_02() {
    String typeName = "com.google.common.collect.AbstractMultimap";
    JvmType _findTypeByName = this.getTypeProvider().findTypeByName(typeName);
    JvmGenericType type = ((JvmGenericType) _findTypeByName);
    JvmFeature _onlyElement = Iterables.<JvmFeature>getOnlyElement(type.findAllFeaturesByName("containsValue"));
    JvmOperation containsValue = ((JvmOperation) _onlyElement);
    Assert.assertNotNull(containsValue);
    JvmFormalParameter firstParam = containsValue.getParameters().get(0);
    Assert.assertEquals(0, firstParam.getAnnotations().size());
  }
  
  @Test
  @Override
  public void testParameterNames_01() {
    this.doTestParameterName(Bug347739ThreeTypeParamsSuperSuper.class, "getToken(A)", "arg0");
  }
  
  @Test
  @Override
  public void testParameterNames_02() {
    this.doTestParameterName(AbstractMethods.class, "abstractMethodWithParameter(java.lang.String)", "arg0");
  }
  
  @Test
  @Override
  public void testParameterNames_03() {
    this.doTestParameterName(ClassWithVarArgs.class, "method(java.lang.String[])", "arg0");
  }
  
  @Ignore
  @Test
  @Override
  public void testFindTypeByName_NestedTypeQualifiedWithSubType_02() {
    super.testFindTypeByName_NestedTypeQualifiedWithSubType_02();
  }
}
