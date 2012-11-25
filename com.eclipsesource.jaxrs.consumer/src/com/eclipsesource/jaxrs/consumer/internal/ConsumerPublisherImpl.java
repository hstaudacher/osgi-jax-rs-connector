package com.eclipsesource.jaxrs.consumer.internal;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class ConsumerPublisherImpl implements ConsumerPublisher {

  private final BundleContext context;
  private final List<ServiceRegistration<?>> registrations;

  public ConsumerPublisherImpl( BundleContext context ) {
    this.registrations = new ArrayList<ServiceRegistration<?>>();
    this.context = context;
  }

  @Override
  public void publishConsumers( String baseUrl, Class<?>[] types, Object[] providers ) {
    for( Class<?> type : types ) {
      Object resource = ConsumerFactory.createConsumer( baseUrl, type, providers );
      ServiceRegistration<?> registration = context.registerService( type.getName(), resource, null );
      registrations.add( registration );
    }
  }

  public void unregister() {
    for( ServiceRegistration<?> registration : registrations ) {
      registration.unregister();
    }
  }
}
