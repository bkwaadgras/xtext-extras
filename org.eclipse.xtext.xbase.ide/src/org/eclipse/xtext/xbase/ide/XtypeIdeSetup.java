/*******************************************************************************
 * Copyright (c) 2010, 2020 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
 package org.eclipse.xtext.xbase.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;
import org.eclipse.xtext.xbase.XtypeRuntimeModule;
import org.eclipse.xtext.xbase.XtypeStandaloneSetup;

/** 
 * Initialization support for running Xtext languages as language servers.
 */
public class XtypeIdeSetup extends XtypeStandaloneSetup {
	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new XtypeRuntimeModule(), new XtypeIdeModule()));
	}
}
