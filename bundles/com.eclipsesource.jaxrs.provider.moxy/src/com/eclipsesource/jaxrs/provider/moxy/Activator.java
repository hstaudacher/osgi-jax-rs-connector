/* ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs */
package com.eclipsesource.jaxrs.provider.moxy;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    MOXyJsonProviderService service = new MOXyJsonProviderService();
    registration = context.registerService( MOXyJsonProviderService.class.getName(), service, null );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    if( registration != null ) {
      registration.unregister();
    }
  }
}
