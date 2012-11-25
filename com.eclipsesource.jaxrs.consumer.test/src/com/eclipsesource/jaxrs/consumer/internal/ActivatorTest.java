package com.eclipsesource.jaxrs.consumer.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class ActivatorTest {
  
  @Test
  @SuppressWarnings( "unchecked" )
  public void testRegistersPublisher() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );
    
    verify( context ).registerService( eq( ConsumerPublisher.class.getName() ), 
                                       any( ConsumerPublisherImpl.class ), 
                                       any( Dictionary.class ) );
  }
  
  @Test
  @SuppressWarnings( {
    "unchecked", "rawtypes"
  } )
  public void testUnregistersPublisher() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    ServiceRegistration registration = mock( ServiceRegistration.class );
    when( context.registerService( anyString(), any(), any( Dictionary.class ) ) ).thenReturn( registration );
    activator.start( context );

    activator.stop( context );
    
    verify( registration ).unregister();
  }
}
