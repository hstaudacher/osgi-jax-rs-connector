/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 *    Dirk Lecluse - added tracking of Provider classes
 *    Frank Appel - specified Filter to exclude resources from publishing
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.internal;

import static com.eclipsesource.jaxrs.connector.ServiceProperties.PUBLISH;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;


public class ResourceTracker extends ServiceTracker<Object, Object> {
  
  static final String ANY_SERVICE_FILTER = "(&(objectClass=*)(!(" + PUBLISH + "=false)))";
  
  private final BundleContext context;
  private final JAXRSConnector connector;
  
  public ResourceTracker( BundleContext context, Filter filter, JAXRSConnector connector ) {
    super( context, filter, null );
    this.context = context;
    this.connector = connector;
  }
  
  @Override
  public Object addingService( ServiceReference<Object> reference ) {
    Object service = context.getService( reference );
    return delegateAddService( reference, service );
  }

  private Object delegateAddService( ServiceReference<Object> reference, Object service ) {
    Object result;
    if( isResource( service ) ) {
      result = connector.addResource( reference );
    } else {
      result = super.addingService( reference );
    }
    return result;
  }

  @Override
  public void removedService( ServiceReference<Object> reference, Object service ) {
    if( isResource( service ) ) {
      connector.removeResource( service );
    }
    context.ungetService( reference );
  }
  
  @Override
  public void modifiedService( ServiceReference<Object> reference, Object service ) {
    if( isResource( service ) ) {
      connector.removeResource( service );
      delegateAddService( reference, service );
    }
  }

  private boolean isResource( Object service ) {
    return service != null && hasRegisterableAnnotation( service );
  }

  private boolean hasRegisterableAnnotation( Object service ) {
    boolean result = isRegisterableAnnotationPresent( service.getClass() );
    if( !result ) {
      Class<?>[] interfaces = service.getClass().getInterfaces();
      for( Class<?> type : interfaces ) {
        result = result || isRegisterableAnnotationPresent( type );
      }
    }
    return result;
  }

  private boolean isRegisterableAnnotationPresent( Class<?> type ) {
    return type.isAnnotationPresent( Path.class ) || type.isAnnotationPresent( Provider.class );
  }
  
}
