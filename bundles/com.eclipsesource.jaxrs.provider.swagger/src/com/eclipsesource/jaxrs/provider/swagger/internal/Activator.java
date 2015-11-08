/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.swagger.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;


public class Activator implements BundleActivator {

  private final List<ServiceRegistration> registrations;

  public Activator() {
    registrations = new ArrayList<>();
  }

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    SwaggerConfiguration swaggerConfiguration = registerSwaggerConfiguration( bundleContext );
    ScannerFactory.setScanner( new OSGiJaxRsScanner( swaggerConfiguration ) );
    registrations.add( bundleContext.registerService( ApiListingResource.class.getName(), new ApiListingResource(), null ) );
    registrations.add( bundleContext.registerService( SwaggerSerializers.class.getName(), new SwaggerSerializers(), null ) );
  }

  private SwaggerConfiguration registerSwaggerConfiguration( BundleContext bundleContext ) {
    SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();
    Dictionary<String, String> properties = new Hashtable<>();
    properties.put( Constants.SERVICE_PID, SwaggerConfiguration.SERVICE_PID );
    registrations.add( bundleContext.registerService( ManagedService.class.getName(), swaggerConfiguration, properties ) );
    return swaggerConfiguration;
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    for( ServiceRegistration registration : registrations ) {
      registration.unregister();
    }
    registrations.clear();
  }
}
