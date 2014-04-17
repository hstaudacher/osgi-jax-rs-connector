/* ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs */
package com.eclipsesource.jaxrs.provider.gson;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    GsonProvider<?> provider = new GsonProvider<Object>();
    registration = bundleContext.registerService( GsonProvider.class.getName(), provider, null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    if( registration != null ) {
      registration.unregister();
    }
  }
}
