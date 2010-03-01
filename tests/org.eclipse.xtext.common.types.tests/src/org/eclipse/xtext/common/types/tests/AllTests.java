package org.eclipse.xtext.common.types.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * automatically generated by org.eclipse.emf.mwe.releng.GenerateTestSuite 
 * 
 * @generated 
 */
public class AllTests {
   public static Test suite() {
      TestSuite suite = new TestSuite("Tests for org.eclipse.xtext.common.types.tests");
      suite.addTestSuite(org.eclipse.xtext.common.types.AnnotationTypeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.ArrayTypeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.ConstructorTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.EnumerationTypeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.FieldTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.FormalParameterTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.GenericTypeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.LowerBoundTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.OperationTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.ParameterizedTypeReferenceTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.PrimitiveTypeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.ReferenceTypeArgumentTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.TypeParameterTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.UpperBoundTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.VoidTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.WildcardTypeArgumentTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactoryTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.TypeResourceTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.ClassFinderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.ClassMirrorTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.ClassNameUtilTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.ClassURIHelperTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.ClasspathTypeProviderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.PrimitiveMirrorTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.PrimitiveTypeFactoryTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.impl.PrimitivesTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderFactoryTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.jdt.MockJavaProjectProviderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.jdt.TypeURIHelperTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.xtext.ClasspathBasedTypeScopeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.xtext.ui.JdtBasedSimpleTypeScopeProviderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.xtext.ui.JdtBasedSimpleTypeScopeTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.access.xtext.ui.XtextResourceSetBasedProjectProviderTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.util.ClasspathSuperTypeCollectorTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.util.JdtSuperTypeCollectorTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.util.ClasspathAssignabilityComputerTest.class);
      suite.addTestSuite(org.eclipse.xtext.common.types.util.JdtAssignabilityComputerTest.class);
      return suite;
   }
}
