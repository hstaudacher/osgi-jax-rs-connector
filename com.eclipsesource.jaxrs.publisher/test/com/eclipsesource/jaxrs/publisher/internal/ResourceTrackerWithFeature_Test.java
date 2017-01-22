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

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;


@RunWith( MockitoJUnitRunner.class )
public class ResourceTrackerWithFeature_Test {
  
  private ResourceTracker resourceTracker;
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference reference;
  @Mock
  private BundleContext context;
  
  private class FakeFeature implements Feature {

    public FakeFeature() {
      // no content
    }

    @Override
    public boolean configure( FeatureContext context ) {
      return false;
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
    FakeFeature fakeFeature = new FakeFeature();
    when( context.getService( reference ) ).thenReturn( fakeFeature );
    
    resourceTracker.addingService( reference );
    
    verify( connector ).addResource( reference );
  }
  
  @Test
  public void delegatesModifyService() {
    FakeFeature fakeFeature = new FakeFeature();
    when( context.getService( reference ) ).thenReturn( fakeFeature );
    
    resourceTracker.modifiedService( reference, fakeFeature );
    
    InOrder order = inOrder( connector );
    order.verify( connector ).removeResource( fakeFeature );
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
    FakeFeature fakeFeature = new FakeFeature();
    when( context.getService( reference ) ).thenReturn( fakeFeature );
    
    resourceTracker.removedService( reference, fakeFeature );
    
    verify( connector ).removeResource( fakeFeature );
    verify( context ).ungetService( reference );
  }
  
  @Test
  public void delegatesRemoveServiceWithoutPath() {
    Object service = new Object();
    when( context.getService( reference ) ).thenReturn( service );
    
    resourceTracker.removedService( reference, service );
    
    verify( connector ).removeResource( service );
    verify( context ).ungetService( reference );
  }
}
