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
package com.eclipsesource.jaxrs.security.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.security.SecurityContextProvider;
import com.eclipsesource.jaxrs.security.filter.ContainerRequestFilterProvider;

public class Activator implements BundleActivator {

  private static Activator instance;
  private ServiceRegistration<ContainerRequestFilterProvider> containerRequestFilterProviderServiceRegistration;
  private ServiceTracker<SecurityContextProvider, SecurityContextProvider> securityContentProviderTracker;

  public static Activator getInstance() {
    return instance;
  }

  public SecurityContextProvider getSecurityContextProvider() {
    return securityContentProviderTracker.getService();
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    containerRequestFilterProviderServiceRegistration = context.registerService( ContainerRequestFilterProvider.class,
                                                                                 new ContainerRequestFilterProvider(),
                                                                                 null );
    securityContentProviderTracker = new ServiceTracker<SecurityContextProvider, SecurityContextProvider>( context,
                                                                                                           SecurityContextProvider.class,
                                                                                                           null );
    securityContentProviderTracker.open();
    instance = this;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    instance = null;
    if( containerRequestFilterProviderServiceRegistration != null )
      containerRequestFilterProviderServiceRegistration.unregister();
    if( securityContentProviderTracker != null )
      securityContentProviderTracker.close();
  }
}
