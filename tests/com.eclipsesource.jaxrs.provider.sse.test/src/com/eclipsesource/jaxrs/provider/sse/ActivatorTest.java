package com.eclipsesource.jaxrs.provider.sse;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class ActivatorTest {

  @Test
  @SuppressWarnings( "unchecked" )
  public void testRegistersSseProviderOnStart() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );
    
    verify( context ).registerService( eq( SseFeature.class ), any( SseFeature.class ), any( Dictionary.class ) );
  }
  
  @Test
  @SuppressWarnings( "unchecked" )
  public void testUnregistersSseProviderOnStop() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    ServiceRegistration<?> registration = mock( ServiceRegistration.class );
    when( context.registerService( any( Class.class ), any( Object.class ), any( Dictionary.class ) ) ).thenReturn( registration );
    activator.start( context );
    
    activator.stop( context );
    
    verify( registration ).unregister();
  }
}
