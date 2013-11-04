/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.security.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class Activator implements BundleActivator {

  private ServiceRegistration<TestResource> resourceRegistration;
  private ServiceRegistration<AuthenticationService> authenticationRegistration;
  private ServiceRegistration<AuthorizationService> authorizationRegistration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    resourceRegistration = bundleContext.registerService( TestResource.class, new TestResource(), null );
    SecurityService securityService = new SecurityService();
    authenticationRegistration = bundleContext.registerService( AuthenticationService.class, securityService, null );
    authorizationRegistration = bundleContext.registerService( AuthorizationService.class, securityService, null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    authenticationRegistration.unregister();
    authorizationRegistration.unregister();
    resourceRegistration.unregister();
  }
}
