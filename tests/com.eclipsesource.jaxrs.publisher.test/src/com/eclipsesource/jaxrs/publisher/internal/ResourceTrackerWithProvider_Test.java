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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

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
public class ResourceTrackerWithProvider_Test {
  
  private ResourceTracker resourceTracker;
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference reference;
  @Mock
  private BundleContext context;
  
  @Provider
  private class FakeProvider implements MessageBodyWriter<Object> {

    public FakeProvider() {
      // no content
    }

    @Override
    public long getSize( Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4 )
    {
      return -1;
    }

    @Override
    public boolean isWriteable( Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3 ) {
      return true;
    }

    @Override
    public void writeTo( Object arg0,
                         Class<?> arg1,
                         Type arg2,
                         Annotation[] arg3,
                         MediaType arg4,
                         MultivaluedMap<String, Object> arg5,
                         OutputStream arg6 ) throws IOException, WebApplicationException
    {
      // do nothing
    }
  }

  @Before
  public void setUp() {
    context = mock( BundleContext.class );
    Filter filter = mock( Filter.class );
    resourceTracker = new ResourceTracker( context, filter, connector );
  }
  
  @Test
  public void delegatesAddServiceWithProvider() {
    FakeProvider fakeProvider = new FakeProvider();
    when( context.getService( reference ) ).thenReturn( fakeProvider );
    
    resourceTracker.addingService( reference );
    
    verify( connector ).addResource( reference );
  }
  
  @Test
  public void delegatesModifyService() {
    FakeProvider fakeProvider = new FakeProvider();
    when( context.getService( reference ) ).thenReturn( fakeProvider );
    
    resourceTracker.modifiedService( reference, fakeProvider );
    
    InOrder order = inOrder( connector );
    order.verify( connector ).removeResource( fakeProvider );
    order.verify( connector ).addResource( reference );
  }
  
  @Test
  public void delegatesAddServiceWithoutProvider() {
    when( context.getService( reference ) ).thenReturn( new Object() );
    
    resourceTracker.addingService( reference );
    
    verify( connector, never() ).addResource( reference );
  }
  
  @Test
  public void delegatesRemoveServiceWithProvider() {
    FakeProvider fakeProvider = new FakeProvider();
    when( context.getService( reference ) ).thenReturn( fakeProvider );
    
    resourceTracker.removedService( reference, fakeProvider );
    
    verify( connector ).removeResource( fakeProvider );
    verify( context ).ungetService( reference );
  }
  
  @Test
  public void delegatesRemoveServiceWithoutProvider() {
    Object service = new Object();
    when( context.getService( reference ) ).thenReturn( service );
    
    resourceTracker.removedService( reference, service );
    
    verify( connector, never() ).removeResource( service );
    verify( context ).ungetService( reference );
  }
}
