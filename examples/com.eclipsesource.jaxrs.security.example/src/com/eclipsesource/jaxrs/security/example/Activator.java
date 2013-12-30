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

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

public class Activator implements BundleActivator {

  private ServiceRegistration<SecureResource> resourceRegistration;
  private ServiceRegistration<AuthenticationHandler> authenticationRegistration;
  private ServiceRegistration<AuthorizationHandler> authorizationRegistration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    resourceRegistration = bundleContext.registerService( SecureResource.class, new SecureResource(), null );
    SecurityHandler securityService = new SecurityHandler();
    authenticationRegistration = bundleContext.registerService( AuthenticationHandler.class, securityService, null );
    authorizationRegistration = bundleContext.registerService( AuthorizationHandler.class, securityService, null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    authenticationRegistration.unregister();
    authorizationRegistration.unregister();
    resourceRegistration.unregister();
  }
}
