package com.eclipsesource.jaxrs.consumer.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    registration = context.registerService( ConsumerPublisher.class.getName(), 
                                            new ConsumerPublisherImpl( context ),
                                            null );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    registration.unregister();
  }
}
