package com.eclipsesource.jaxrs.connector.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.connector.internal.ServiceContainer.ServiceHolder;


public class ServiceContainer_Test {
  
  private BundleContext bundleContext;
  private ServiceContainer< Object > container;
  private ServiceReference< Object > serviceReference;
  
  @Before
  public void setUp() {
    bundleContext = mock( BundleContext.class );
    container = new ServiceContainer< Object >( bundleContext );
  }
  
  @SuppressWarnings( "unchecked" )
  private void mockServiceReference( Object service ) {
    serviceReference = mock( ServiceReference.class );
    when( bundleContext.getService( serviceReference ) ).thenReturn( service );
  }

  @Test
  public void testAddService() {
    Object service = new Object();
    
    ServiceHolder< Object > holder1 = container.add( service );
    ServiceHolder< Object > holder2 = container.add( service );
    
    assertEquals( 1, container.size() );
    assertSame( holder1, holder2 );
    assertSame( service, holder1.getService());
  }
  
  @Test
  public void testAddServiceReference() {
    Object service = new Object();
    mockServiceReference( service );
    
    ServiceHolder< Object > holder1 = container.add( serviceReference );
    ServiceHolder< Object > holder2 = container.add( serviceReference );
    ServiceHolder< Object > holder3 = container.add( service );
    
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
    
    ServiceHolder< Object > holder1 = container.add( service );
    ServiceReference< Object > reference = holder1.getReference();
    ServiceHolder< Object > holder2 = container.add( serviceReference );
    
    assertEquals( 1, container.size() );
    assertSame( holder1, holder2 );
    assertNull( reference );
    assertSame( serviceReference, holder2.getReference() );
  }
  
  @Test
  public void testFind() {
    Object service = new Object();
    container.add( service );

    ServiceHolder< Object > holder = container.find( service );
    
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
    ServiceHolder< Object > holder = container.add( service );
    
    ServiceHolder< Object >[] services = container.getServices();
    
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