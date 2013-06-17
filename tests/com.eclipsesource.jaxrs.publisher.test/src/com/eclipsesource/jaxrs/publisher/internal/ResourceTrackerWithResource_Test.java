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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.Path;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.publisher.internal.JAXRSConnector;
import com.eclipsesource.jaxrs.publisher.internal.ResourceTracker;


@RunWith( MockitoJUnitRunner.class )
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ResourceTrackerWithResource_Test {
  
  private ResourceTracker resourceTracker;
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference reference;
  @Mock
  private BundleContext context;
  
  @Path( "test" )
  private class FakeResource {

    public FakeResource() {
      // no content
    }
  }

  @Before
  public void setUp() {
    context = mock( BundleContext.class );
    Filter filter = mock( Filter.class );
    resourceTracker = new ResourceTracker( context, filter, connector );
  }
  
  @Test
  public void delegatesAddServiceWithPath() {
    FakeResource fakeResource = new FakeResource();
    when( context.getService( reference ) ).thenReturn( fakeResource );
    
    resourceTracker.addingService( reference );
    
    verify( connector ).addResource( reference );
  }
  
  @Test
  public void delegatesModifyService() {
    FakeResource fakeResource = new FakeResource();
    when( context.getService( reference ) ).thenReturn( fakeResource );
    
    resourceTracker.modifiedService( reference, fakeResource );
    
    InOrder order = inOrder( connector );
    order.verify( connector ).removeResource( fakeResource );
    order.verify( connector ).addResource( reference );
  }
  
  @Test
  public void delegatesAddServiceWithoutPath() {
    when( context.getService( reference ) ).thenReturn( new Object() );
    
    resourceTracker.addingService( reference );
    
    verify( connector, never() ).addResource( reference );
  }
  
  @Test
  public void delegatesRemoveServiceWithPath() {
    FakeResource fakeResource = new FakeResource();
    when( context.getService( reference ) ).thenReturn( fakeResource );
    
    resourceTracker.removedService( reference, fakeResource );
    
    verify( connector ).removeResource( fakeResource );
    verify( context ).ungetService( reference );
  }
  
  @Test
  public void delegatesRemoveServiceWithoutPath() {
    Object service = new Object();
    when( context.getService( reference ) ).thenReturn( service );
    
    resourceTracker.removedService( reference, service );
    
    verify( connector, never() ).removeResource( service );
    verify( context ).ungetService( reference );
  }
}
