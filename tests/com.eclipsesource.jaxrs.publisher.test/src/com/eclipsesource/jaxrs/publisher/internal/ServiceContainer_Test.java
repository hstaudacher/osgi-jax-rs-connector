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
package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.publisher.internal.ServiceContainer.ServiceHolder;


public class ServiceContainer_Test {
  
  private BundleContext bundleContext;
  private ServiceContainer container;
  private ServiceReference serviceReference;
  
  @Before
  public void setUp() {
    bundleContext = mock( BundleContext.class );
    container = new ServiceContainer( bundleContext );
  }
  
  private void mockServiceReference( Object service ) {
    serviceReference = mock( ServiceReference.class );
    when( bundleContext.getService( serviceReference ) ).thenReturn( service );
  }

  @Test
  public void testAddService() {
    Object service = new Object();
    
    ServiceHolder holder1 = container.add( service );
    ServiceHolder holder2 = container.add( service );
    
    assertEquals( 1, container.size() );
    assertSame( holder1, holder2 );
    assertSame( service, holder1.getService());
  }
  
  @Test
  public void testAddServiceReference() {
    Object service = new Object();
    mockServiceReference( service );
    
    ServiceHolder holder1 = container.addReference( serviceReference );
    ServiceHolder holder2 = container.addReference( serviceReference );
    ServiceHolder holder3 = container.add( service );
    
    assertEquals( 1, container.size() );
    assertSame( holder1, holder2 );
    assertSame( holder1, holder3 );
    assertSame( serviceReference, holder1.getReference() );
    assertSame( service, holder1.getService());
  }
  
  @Test
  public void testUpdateServiceReference() {
    Object service = new Object();
    mockServiceReference( service );
    
    ServiceHolder holder1 = container.add( service );
    ServiceReference reference = holder1.getReference();
    ServiceHolder holder2 = container.addReference( serviceReference );
    
    assertEquals( 1, container.size() );
    assertSame( holder1, holder2 );
    assertNull( reference );
    assertSame( serviceReference, holder2.getReference() );
  }
  
  @Test
  public void testFind() {
    Object service = new Object();
    container.add( service );

    ServiceHolder holder = container.find( service );
    
    assertSame( service, holder.getService() );
  }
  
  @Test
  public void testClear() {
    Object service = new Object();
    container.add( service );
    
    container.clear();
    
    assertEquals( 0, container.size() );
  }
  
  @Test
  public void testGetServices() {
    Object service = new Object();
    ServiceHolder holder = container.add( service );
    
    ServiceHolder[] services = container.getServices();
    
    assertEquals( 1, services.length );
    assertSame( holder.getService(), services[ 0 ].getService() );
  }
  
  @Test
  public void testRemove() {
    Object service = new Object();
    container.add( service );
    
    container.remove( service );
    
    assertEquals( 0, container.size() );
  }
  
}