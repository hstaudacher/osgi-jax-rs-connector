package com.eclipsesource.jaxrs.provider.sse;

import org.glassfish.jersey.media.sse.SseFeature;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private ServiceRegistration<SseFeature> registration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    registration = bundleContext.registerService( SseFeature.class, new SseFeature(), null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    if( registration != null ) {
      registration.unregister();
    }
  }
}
