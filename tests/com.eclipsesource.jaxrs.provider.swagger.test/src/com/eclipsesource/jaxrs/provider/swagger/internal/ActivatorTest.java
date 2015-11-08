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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;


public class ActivatorTest {

  private Activator activator;

  @Before
  public void setUp() {
    activator = new Activator();
  }

  @Test
  public void testRegistersApiListeningResource() throws Exception {
    BundleContext context = mock( BundleContext.class );

    activator.start( context );

    verify( context ).registerService( eq( ApiListingResource.class.getName() ),
                                       any( ApiListingResource.class ),
                                       any( Dictionary.class ) );
  }

  @Test
  public void testRegistersSwaggerSerializers() throws Exception {
    BundleContext context = mock( BundleContext.class );

    activator.start( context );

    verify( context ).registerService( eq( SwaggerSerializers.class.getName() ),
                                       any( SwaggerSerializers.class ),
                                       any( Dictionary.class ) );
  }

  @Test
  @SuppressWarnings( "rawtypes" )
  public void testRegistersSwaggerConfiguration() throws Exception {
    BundleContext context = mock( BundleContext.class );

    activator.start( context );

    ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass( Dictionary.class );
    verify( context ).registerService( eq( ManagedService.class.getName() ),
                                       any( SwaggerConfiguration.class ),
                                       captor.capture() );
    assertEquals( captor.getValue().get( Constants.SERVICE_PID ), "com.eclipsesource.jaxrs.swagger.config" );
  }
}
