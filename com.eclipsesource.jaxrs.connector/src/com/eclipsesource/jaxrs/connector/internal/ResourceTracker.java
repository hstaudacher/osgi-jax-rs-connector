package com.eclipsesource.jaxrs.connector.internal;

import javax.ws.rs.Path;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;


// TODO: check if we can remove the suppress warnings
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ResourceTracker extends ServiceTracker {
  
  static final String ANY_SERVICE_FILTER = "(objectClass=*)";
  
  private final BundleContext context;
  private final JAXRSConnector connector;
  
  public ResourceTracker( BundleContext context, Filter filter, JAXRSConnector connector ) {
    super( context, filter, null );
    this.context = context;
    this.connector = connector;
  }
  
  @Override
  public Object addingService( ServiceReference reference ) {
    Object service = context.getService( reference );
    return delegateAddService( reference, service );
  }

  private Object delegateAddService( ServiceReference reference, Object service ) {
    Object result;
    if( isResource( service ) ) {
      result = connector.addResource( reference );
    } else {
      result = super.addingService( reference );
    }
    return result;
  }

  @Override
  public void removedService( ServiceReference reference, Object service ) {
    if( isResource( service ) ) {
      connector.removeResource( service );
    }
    context.ungetService( reference );
  }
  
  @Override
  public void modifiedService( ServiceReference reference, Object service ) {
    if( isResource( service ) ) {
      connector.removeResource( service );
      delegateAddService( reference, service );
    }
  }

  private boolean isResource( Object service ) {
    return service != null && service.getClass().isAnnotationPresent( Path.class );
  }
  
}
