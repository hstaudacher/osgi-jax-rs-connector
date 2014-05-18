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
package com.eclipsesource.jaxrs.consumer.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class ConsumerPublisherImplTest {
  
  @Path( "/test1")
  public static interface Resource1 {
    @GET
    void get();
  }
  
  @Path( "/test2")
  public static interface Resource2 {
    @GET
    void get();
  }
  
  @Test
  public void testPublishesResourcesAsServices() {
    BundleContext context = mock( BundleContext.class );
    ConsumerPublisherImpl publisher = new ConsumerPublisherImpl( context );
    
    publisher.publishConsumers( "http://localhost", new Class[] { Resource1.class, Resource2.class }, null );
    
    verify( context ).registerService( eq( Resource1.class.getName() ), any( Resource1.class ), any( Dictionary.class ) );
    verify( context ).registerService( eq( Resource2.class.getName() ), any( Resource2.class ), any( Dictionary.class ) );
  }
  
  @Test
  @SuppressWarnings( "rawtypes" )
  public void testDoesExcludeResourcesFromConnectorPublishing() {
    BundleContext context = mock( BundleContext.class );
    ConsumerPublisherImpl publisher = new ConsumerPublisherImpl( context );
    
    publisher.publishConsumers( "http://localhost", new Class[] { Resource1.class }, null );
    
    ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass( Dictionary.class );
    verify( context ).registerService( eq( Resource1.class.getName() ), any( Resource1.class ), captor.capture() );
    Object property = captor.getValue().get( "com.eclipsesource.jaxrs.publish" );
    assertNotNull( property );
    assertEquals( "false", property );
  }
  
  @Test
  public void testUnregisterServices() {
    BundleContext context = mock( BundleContext.class );
    ConsumerPublisherImpl publisher = new ConsumerPublisherImpl( context );
    ServiceRegistration registration = mock( ServiceRegistration.class );
    when( context.registerService( anyString(), any(), any( Dictionary.class ) ) ).thenReturn( registration );
    publisher.publishConsumers( "http://localhost", new Class[] { Resource1.class, Resource2.class }, null );
    
    publisher.unregister();
    
    verify( registration, times( 2 ) ).unregister();
  }
}
