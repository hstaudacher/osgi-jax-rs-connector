/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import com.eclipsesource.jaxrs.connector.internal.ServiceContainer.ServiceHolder;


public class JAXRSConnector {
  
  private static final String HTTP_SERVICE_PORT_PROPERTY = "org.osgi.service.http.port";
  private static final String RESOURCE_HTTP_PORT_PROPERTY = "http.port";
  private static final String DEFAULT_HTTP_PORT = "80";
  
  private final Object lock = new Object();
  private final ServiceContainer<HttpService> httpServices;
  private final ServiceContainer<Object> resources;
  private final Map<HttpService, JerseyContext> contextMap;
  private final BundleContext bundleContext;
  private final List<ServiceHolder<?>> resourceCache;

  JAXRSConnector( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
    this.httpServices = new ServiceContainer<HttpService>( bundleContext );
    this.resources = new ServiceContainer<Object>( bundleContext );
    this.contextMap = new HashMap<HttpService, JerseyContext>();
    this.resourceCache = new ArrayList<ServiceHolder<?>>();
  }
  
  HttpService addHttpService( ServiceReference<HttpService> reference ) {
    synchronized( lock ) {
      return doAddHttpService( reference );
    }
  }

  private HttpService doAddHttpService( ServiceReference<HttpService> reference ) {
    ServiceHolder<HttpService> serviceHolder = httpServices.add( reference );
    HttpService service = serviceHolder.getService();
    contextMap.put( service, createJerseyContext( service ) );
    clearCache();
    return service;
  }

  private void clearCache() {
    ArrayList<ServiceHolder<?>> cache = new ArrayList<ServiceHolder<?>>( resourceCache );
    resourceCache.clear();
    for( ServiceHolder<?> serviceHolder : cache ) {
      registerResource( serviceHolder );
    }
  }

  void removeHttpService( HttpService service ) {
    synchronized( lock ) {
      doRemoveHttpService( service );
    }
  }

  private void doRemoveHttpService( HttpService service ) {
    JerseyContext context = contextMap.remove( service );
    if( context != null ) {
      cacheFreedResources( context );
    }
    httpServices.remove( service );
  }

  private void cacheFreedResources( JerseyContext context ) {
    List<Object> freeResources = context.eliminate();
    for( Object resource : freeResources ) {
      resourceCache.add( resources.find( resource ) );
    }
  }

  Object addResource( ServiceReference<Object> reference ) {
    synchronized( lock ) {
      return doAddResource( reference );
    }
  }

  private Object doAddResource( ServiceReference<Object> reference ) {
    ServiceHolder<Object> serviceHolder = resources.add( reference );
    registerResource( serviceHolder );
    return serviceHolder.getService();
  }

  private void registerResource( ServiceHolder<?> serviceHolder ) {
    Object port = getPort( serviceHolder );
    registerResource( serviceHolder, port );
  }

  private Object getPort( ServiceHolder<?> serviceHolder ) {
    Object port = serviceHolder.getReference().getProperty( RESOURCE_HTTP_PORT_PROPERTY );
    if( port == null ) {
      port = bundleContext.getProperty( HTTP_SERVICE_PORT_PROPERTY );
      if( port == null ) {
        port = DEFAULT_HTTP_PORT;
      }
    }
    return port;
  }

  private void registerResource( ServiceHolder<?> serviceHolder, Object port ) {
    HttpService service = findHttpServiceForPort( port );
    if( service != null ) {
      JerseyContext jerseyContext = contextMap.get( service );
      jerseyContext.addResource( serviceHolder.getService() );
    } else {
      cacheResource( serviceHolder );
    }
  }

  private void cacheResource( ServiceHolder<?> serviceHolder ) {
    resourceCache.add( serviceHolder );
  }

  private HttpService findHttpServiceForPort(Object port) {
    ServiceHolder<HttpService>[] serviceHolders = httpServices.getServices();
    HttpService result = null;
    for( ServiceHolder<HttpService> serviceHolder : serviceHolders ) {
      Object servicePort = serviceHolder.getReference().getProperty( RESOURCE_HTTP_PORT_PROPERTY );
      if( servicePort.equals( port ) ) {
        result = serviceHolder.getService();
      }
    }
    return result;
  }

  void removeResource( Object resource ) {
    synchronized( lock ) {
      doRemoveResource( resource );
    }
  }

  private void doRemoveResource( Object resource ) {
    ServiceHolder<Object> serviceHolder = resources.find( resource );
    resourceCache.remove( serviceHolder );
    HttpService httpService = findHttpServiceForPort( getPort( serviceHolder ) );
    removeResourcesFromContext( resource, httpService );
    resources.remove( resource );
  }

  private void removeResourcesFromContext( Object resource, HttpService httpService ) {
    JerseyContext jerseyContext = contextMap.get( httpService );
    if( jerseyContext != null ) {
      jerseyContext.removeResource( resource );
    }
  }

  // For testing purpose
  JerseyContext createJerseyContext( HttpService service ) {
    return new JerseyContext( service );
  }
  
}
