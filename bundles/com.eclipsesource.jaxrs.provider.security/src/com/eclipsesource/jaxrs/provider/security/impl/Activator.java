/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Holger Staudacher - API finalization
 *    ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

public class Activator implements BundleActivator {

  private static Activator instance;
  
  private ServiceTracker authenticationHandlerTracker;
  private ServiceTracker authorizationHandlerTracker;
  private ServiceRegistration rolesAllowedDynamicFeatureRegistration;
  private ServiceRegistration containerRequestFilterRegistration;

  public static Activator getInstance() {
    return instance;
  }
  
  public AuthenticationHandler getAuthenticationHandler() {
    return (AuthenticationHandler)authenticationHandlerTracker.getService();
  }

  public AuthorizationHandler getAuthorizationHandler() {
    return (AuthorizationHandler)authorizationHandlerTracker.getService();
  }
  
  @Override
  public void start( BundleContext context ) throws Exception {
    registerProviderServices( context );
    createHandlerTrackers( context );
    instance = this;
  }

  private void registerProviderServices( BundleContext context ) {
    rolesAllowedDynamicFeatureRegistration = context.registerService( RolesAllowedDynamicFeatureImpl.class.getName(),
                                                                      new RolesAllowedDynamicFeatureImpl(),
                                                                      null );
    containerRequestFilterRegistration = context.registerService( ContainerRequestFilterImpl.class.getName(),
                                                                  new ContainerRequestFilterImpl( new SecurityAdmin() ),
                                                                  null );
  }

  private void createHandlerTrackers( BundleContext context ) {
    authenticationHandlerTracker = new ServiceTracker( context, AuthenticationHandler.class.getName(), null );
    authenticationHandlerTracker.open();
    authorizationHandlerTracker = new ServiceTracker( context, AuthorizationHandler.class.getName(), null );
    authorizationHandlerTracker.open();
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    instance = null;
    unregisterProviderServices();
    closeHandlerTrackers();
  }

  private void unregisterProviderServices() {
    if( rolesAllowedDynamicFeatureRegistration != null ) {
      rolesAllowedDynamicFeatureRegistration.unregister();
    }
    if( containerRequestFilterRegistration != null ) {
      containerRequestFilterRegistration.unregister();
    }
  }

  private void closeHandlerTrackers() {
    if( authenticationHandlerTracker != null ) {
      authenticationHandlerTracker.close();
    }
    if( authorizationHandlerTracker != null ) {
      authorizationHandlerTracker.close();
    }
  }

  // for testing purpose only
  static void setInstance( Activator activator ) {
    instance = activator;
  }
}
