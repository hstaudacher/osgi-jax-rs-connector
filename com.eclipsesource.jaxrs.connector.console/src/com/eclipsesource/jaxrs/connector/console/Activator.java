package com.eclipsesource.jaxrs.connector.console;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    CommandProvider provider = new JaxRsCommandProvider( context );
    registration = context.registerService( CommandProvider.class.getName(), provider, null );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    registration.unregister();
  }
}
