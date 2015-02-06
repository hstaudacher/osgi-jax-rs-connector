package com.eclipsesource.jaxrs.publisher.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.publisher.ServletConfigurationService;

/**
 * Tracker for OSGi Services implementing the {@link ServletConfigurationService} interface. 
 * 
 * @author Ivan Iliev
 *
 */
public class ServletConfigurationTracker extends ServiceTracker {

  private final JAXRSConnector connector;

  ServletConfigurationTracker( BundleContext context, JAXRSConnector connector ) {
    super( context, ServletConfigurationService.class.getName(), null );
    this.connector = connector;
  }

  @Override
  public Object addingService( ServiceReference reference ) {
        return connector.setServletConfiguration(reference);
  }

  @Override
  public void removedService( ServiceReference reference, Object service ) {
    if(service instanceof ServletConfigurationService) {
      connector.unsetServletConfiguration( reference, (ServletConfigurationService)service );
    }
  }
}
