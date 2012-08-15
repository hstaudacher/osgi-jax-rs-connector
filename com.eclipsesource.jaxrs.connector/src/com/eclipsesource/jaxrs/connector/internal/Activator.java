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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.ws.rs.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> connectorRegistration;
  private JAXRSConnector jaxRsConnector;
  private HttpTracker httpTracker;
  private ResourceTracker allTracker;
  private ServiceRegistration<?> configRegistration;

  @Override
  public void start( BundleContext context ) throws Exception {
    System.setProperty( "javax.ws.rs.ext.RuntimeDelegate", 
                        "com.sun.jersey.server.impl.provider.RuntimeDelegateImpl" );
    startJerseyServer();
    jaxRsConnector = new JAXRSConnector( context );
    registerConfiguration( context );
    connectorRegistration = context.registerService( JAXRSConnector.class.getName(), jaxRsConnector, null );
    openHttpServiceTracker( context );
    openAllServiceTracker( context );
  }

  private void registerConfiguration( BundleContext context ) {
    Dictionary<String, String> properties = new Hashtable<String, String>();
    properties.put( Constants.SERVICE_PID, Configuration.CONFIG_SERVICE_PID );
    configRegistration = context.registerService( ManagedService.class.getName(),
                                                  new Configuration( jaxRsConnector ),
                                                  properties );
  }

  private void startJerseyServer() throws BundleException {
    Bundle bundle = getJerseyAPIBundle();
    if( bundle.getState() != Bundle.ACTIVE ) {
      bundle.start();
    }
  }

  private void openHttpServiceTracker( BundleContext context ) {
    httpTracker = new HttpTracker( context, jaxRsConnector );
    httpTracker.open();
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
    connectorRegistration.unregister();
    configRegistration.unregister();
  }

  // For testing purpose
  Bundle getJerseyAPIBundle() {
    return FrameworkUtil.getBundle( Path.class );
  }
}
