/**
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.xbase.compiler;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.util.JavaVersion;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Holger Schill - Initial contribution and API
 * @author Miro Spoenemann
 */
@Accessors
@SuppressWarnings("all")
public class GeneratorConfig {
  /**
   * Whether expressions shall be generated.
   */
  private boolean generateExpressions = true;
  
  /**
   * Whether <code>@SuppressWarnings</code> shall be generated for non-nested types.
   */
  private boolean generateSyntheticSuppressWarnings = true;
  
  /**
   * Whether <code>@Generated</code> shall be generated for non-nested types.
   */
  private boolean generateGeneratedAnnotation = false;
  
  /**
   * Whether the current time shall be added to <code>@Generated</code> annotations.
   */
  private boolean includeDateInGeneratedAnnotation = false;
  
  /**
   * The comment that is added to <code>@Generated</code> annotations, also supports variables:
   * <ul>
   *   <li><code>${sourcefile}</code></li>
   * </ul>
   */
  private String generatedAnnotationComment;
  
  /**
   * Version of the Java Source code that's being generated by the compiler.
   */
  private JavaVersion javaSourceVersion = JavaVersion.JAVA5;
  
  /**
   * Copy the values of the given generator configuration.
   */
  public GeneratorConfig copy(final GeneratorConfig other) {
    this.generateExpressions = other.generateExpressions;
    this.generateSyntheticSuppressWarnings = other.generateSyntheticSuppressWarnings;
    this.generateGeneratedAnnotation = other.generateGeneratedAnnotation;
    this.includeDateInGeneratedAnnotation = other.includeDateInGeneratedAnnotation;
    this.generatedAnnotationComment = other.generatedAnnotationComment;
    this.javaSourceVersion = other.javaSourceVersion;
    return this;
  }
  
  @Pure
  public boolean isGenerateExpressions() {
    return this.generateExpressions;
  }
  
  public void setGenerateExpressions(final boolean generateExpressions) {
    this.generateExpressions = generateExpressions;
  }
  
  @Pure
  public boolean isGenerateSyntheticSuppressWarnings() {
    return this.generateSyntheticSuppressWarnings;
  }
  
  public void setGenerateSyntheticSuppressWarnings(final boolean generateSyntheticSuppressWarnings) {
    this.generateSyntheticSuppressWarnings = generateSyntheticSuppressWarnings;
  }
  
  @Pure
  public boolean isGenerateGeneratedAnnotation() {
    return this.generateGeneratedAnnotation;
  }
  
  public void setGenerateGeneratedAnnotation(final boolean generateGeneratedAnnotation) {
    this.generateGeneratedAnnotation = generateGeneratedAnnotation;
  }
  
  @Pure
  public boolean isIncludeDateInGeneratedAnnotation() {
    return this.includeDateInGeneratedAnnotation;
  }
  
  public void setIncludeDateInGeneratedAnnotation(final boolean includeDateInGeneratedAnnotation) {
    this.includeDateInGeneratedAnnotation = includeDateInGeneratedAnnotation;
  }
  
  @Pure
  public String getGeneratedAnnotationComment() {
    return this.generatedAnnotationComment;
  }
  
  public void setGeneratedAnnotationComment(final String generatedAnnotationComment) {
    this.generatedAnnotationComment = generatedAnnotationComment;
  }
  
  @Pure
  public JavaVersion getJavaSourceVersion() {
    return this.javaSourceVersion;
  }
  
  public void setJavaSourceVersion(final JavaVersion javaSourceVersion) {
    this.javaSourceVersion = javaSourceVersion;
  }
}
