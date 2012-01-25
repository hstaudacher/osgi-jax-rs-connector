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
package com.eclipsesource.jaxrs.connector.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;

import com.sun.jersey.api.core.ResourceConfig;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;
  private JAXRSConnector jaxRsConnector;
  private HttpTracker httpTracker;
  private ResourceTracker allTracker;
  private ModelClassProviderTracker modelTracker;
  static Activator INSTANCE;

  @Override
  public void start( BundleContext context ) throws Exception {
    startJerseyServer();
    INSTANCE = this;
    jaxRsConnector = new JAXRSConnector( context );
    registration = context.registerService( JAXRSConnector.class.getName(), jaxRsConnector, null );
    
    openHttpServiceTracker( context );
    openModelClassesTracker( context );
    openAllServiceTracker( context );
  }

  private void startJerseyServer() throws BundleException {
    Bundle bundle = getJerseyServerBundle();
    if( bundle.getState() != Bundle.ACTIVE ) {
      bundle.start();
    }
  }

  private void openHttpServiceTracker( BundleContext context ) {
    httpTracker = new HttpTracker( context, jaxRsConnector );
    httpTracker.open();
  }
  
  private void openModelClassesTracker(BundleContext context) {
    modelTracker = new ModelClassProviderTracker( context );
    modelTracker.open();
  }

  private void openAllServiceTracker( BundleContext context ) throws InvalidSyntaxException {
    Filter filter = context.createFilter( ResourceTracker.ANY_SERVICE_FILTER );
    allTracker = new ResourceTracker( context, filter, jaxRsConnector );
    allTracker.open();
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    httpTracker.close();
    allTracker.close();
    modelTracker.close();
    registration.unregister();
    INSTANCE = null;
  }

  // For testing purpose
  Bundle getJerseyServerBundle() {
    return FrameworkUtil.getBundle( ResourceConfig.class );
  }
  
  
  void registerJaxbcontextProvider( DelegatingJAXBContextProvider jaxbcontextProvider ) {
    modelTracker.addDelegatingJAXBContextProvider( jaxbcontextProvider );
  }
}
