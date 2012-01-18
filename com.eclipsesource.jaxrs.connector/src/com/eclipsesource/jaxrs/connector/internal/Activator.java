package com.eclipsesource.jaxrs.connector.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;
  private JAXRSConnector jaxRsConnector;
  private HttpTracker httpTracker;
  private ResourceTracker allTracker;

  @Override
  public void start( BundleContext context ) throws Exception {
    jaxRsConnector = new JAXRSConnector( context );
    registration = context.registerService( JAXRSConnector.class.getName(), jaxRsConnector, null );
    openHttpServiceTracker( context );
    openAllServiceTracker( context );
  }
  
  private void openHttpServiceTracker( BundleContext context ) {
    httpTracker = new HttpTracker( context, jaxRsConnector );
    httpTracker.open();
  }

  private void openAllServiceTracker( BundleContext context ) throws InvalidSyntaxException {
    Filter filter = context.createFilter( ResourceTracker.ANY_SERVICE_FILTER );
    allTracker = new ResourceTracker( context, filter, jaxRsConnector );
    allTracker.open();
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    httpTracker.close();
    allTracker.close();
    registration.unregister();
  }
}
