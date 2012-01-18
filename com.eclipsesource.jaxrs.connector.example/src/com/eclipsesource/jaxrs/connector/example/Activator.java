package com.eclipsesource.jaxrs.connector.example;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    Hashtable<String, String> properties = new Hashtable<String, String>();
    properties.put( Constants.SERVICE_PID, ExampleService.class.getName() );
    ExampleService exampleService = new ExampleService();
    registration = context.registerService( ExampleService.class.getName(), exampleService, properties );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    registration.unregister();
  }
}
