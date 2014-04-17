/* ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs */
package com.eclipsesource.jaxrs.sse.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    registration = bundleContext.registerService( ExampleService.class.getName(), new ExampleService(), null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    registration.unregister();
  }
}
