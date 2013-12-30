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
  
  private ServiceTracker<AuthenticationHandler, AuthenticationHandler> authenticationHandlerTracker;
  private ServiceTracker<AuthorizationHandler, AuthorizationHandler> authorizationHandlerTracker;
  private ServiceRegistration<RolesAllowedDynamicFeatureImpl> rolesAllowedDynamicFeatureRegistration;
  private ServiceRegistration<ContainerRequestFilterImpl> containerRequestFilterRegistration;

  public static Activator getInstance() {
    return instance;
  }
  
  public AuthenticationHandler getAuthenticationHandler() {
    return authenticationHandlerTracker.getService();
  }

  public AuthorizationHandler getAuthorizationHandler() {
    return authorizationHandlerTracker.getService();
  }
  
  @Override
  public void start( BundleContext context ) throws Exception {
    registerProviderServices( context );
    createHandlerTrackers( context );
    instance = this;
  }

  private void registerProviderServices( BundleContext context ) {
    rolesAllowedDynamicFeatureRegistration = context.registerService( RolesAllowedDynamicFeatureImpl.class,
                                                                      new RolesAllowedDynamicFeatureImpl(),
                                                                      null );
    containerRequestFilterRegistration = context.registerService( ContainerRequestFilterImpl.class,
                                                                  new ContainerRequestFilterImpl( new SecurityAdmin() ),
                                                                  null );
  }

  private void createHandlerTrackers( BundleContext context ) {
    authenticationHandlerTracker = new ServiceTracker<AuthenticationHandler, AuthenticationHandler>( context,
                                                                                                     AuthenticationHandler.class,
                                                                                                     null );
    authenticationHandlerTracker.open();
    authorizationHandlerTracker = new ServiceTracker<AuthorizationHandler, AuthorizationHandler>( context,
                                                                                                  AuthorizationHandler.class,
                                                                                                  null );
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
