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
package com.eclipsesource.jaxrs.consumer.example.caller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.consumer.example.GitHubUser;
import com.eclipsesource.jaxrs.consumer.example.GitHubUsers;

public class Activator implements BundleActivator {

  private ServiceReference<GitHubUsers> reference;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    reference = bundleContext.getServiceReference( GitHubUsers.class );
    GitHubUsers gitHubUsers = bundleContext.getService( reference );
    GitHubUser user = gitHubUsers.getUser( "hstaudacher" );
    System.out.println( user );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    bundleContext.ungetService( reference );
  }
}
