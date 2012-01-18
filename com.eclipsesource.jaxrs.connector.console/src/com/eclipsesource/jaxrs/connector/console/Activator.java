/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.console;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    CommandProvider provider = new JaxRsCommandProvider( context );
    registration = context.registerService( CommandProvider.class.getName(), provider, null );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    registration.unregister();
  }
}
