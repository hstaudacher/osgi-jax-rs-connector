/*******************************************************************************
 * Copyright (c) 2012,2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    Dragos Dascalita  - disbaled autodiscovery
 *    Lars Pfannenschmidt  - made WADL generation configurable
 *    Ivan Iliev - added ServletConfiguration handling
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.eclipsesource.jaxrs.publisher.ApplicationConfiguration;
import com.eclipsesource.jaxrs.publisher.ServletConfiguration;
import com.eclipsesource.jaxrs.publisher.internal.ServiceContainer.ServiceHolder;


@SuppressWarnings( "rawtypes" )
public class JerseyContext {

  private final RootApplication application;
  private final HttpService httpService;
  private final ServletContainerBridge servletContainerBridge;
  private final Configuration configuration;
  private final ServletConfiguration servletConfiguration;
  private final ResourcePublisher resourcePublisher;
  private boolean isApplicationRegistered;
  
  public JerseyContext( HttpService httpService, Configuration configuration, ServletConfiguration servletConfiguration, ServiceContainer applicationConfigurations ) {
    this.httpService = httpService;
    this.configuration = configuration;
    this.application = new RootApplication();
    applyApplicationConfigurations( applicationConfigurations );
    this.servletContainerBridge = new ServletContainerBridge( application );
    this.servletConfiguration = servletConfiguration;
    this.resourcePublisher = new ResourcePublisher( servletContainerBridge, configuration.getPublishDelay() );
  }

  private void applyApplicationConfigurations( ServiceContainer applicationConfigurations ) {
    application.addProperties( new DefaultApplicationConfiguration( configuration ).getProperties() );
    ServiceHolder[] services = applicationConfigurations.getServices();
    for( ServiceHolder serviceHolder : services ) {
      Object service = serviceHolder.getService();
      if( service instanceof ApplicationConfiguration ) {
        Map<String, Object> properties = ( ( ApplicationConfiguration )service ).getProperties();
        if( properties != null ) {
          application.addProperties( properties );
        }
      }
    }
  }

  public void addResource( Object resource ) {
    getRootApplication().addResource( resource );
    registerServletWhenNotAlreadyRegistered();
    resourcePublisher.schedulePublishing();
  }

  void registerServletWhenNotAlreadyRegistered() {
    if( !isApplicationRegistered ) {
      isApplicationRegistered = true;
      registerApplication();
    }
  }

  private void registerApplication() {
    ClassLoader loader = getContextClassloader();
    setContextClassloader();
    try {
      registerServlet();
    } catch( ServletException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    } catch( NamespaceException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    } finally {
      resetContextClassloader( loader );
    }
  }

  private ClassLoader getContextClassloader() {
    return Thread.currentThread().getContextClassLoader();
  }

  private void setContextClassloader() {
    Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
  }

  private void registerServlet() throws ServletException, NamespaceException {
    ClassLoader original = getContextClassloader();
    try {
      Thread.currentThread().setContextClassLoader( Application.class.getClassLoader() );
      httpService.registerServlet( configuration.getRoothPath(), 
                                   servletContainerBridge.getServletContainer(), 
                                   getInitParams(), 
                                   getHttpContext() );
    } finally {
      resetContextClassloader( original );
    }
  }

  private Dictionary getInitParams() {
    if( servletConfiguration != null ) {
      return servletConfiguration.getInitParams( httpService, configuration.getRoothPath() );
    }
    return null;
  }

  private HttpContext getHttpContext() {
    if( servletConfiguration != null ) {
      return servletConfiguration.getHttpContext( httpService, configuration.getRoothPath() );
    }
    return null;
  }

  private void resetContextClassloader( ClassLoader loader ) {
    Thread.currentThread().setContextClassLoader( loader );
  }
  
  public void removeResource( Object resource ) {
    getRootApplication().removeResource( resource );
    unregisterServletWhenNoresourcePresents();
    resourcePublisher.schedulePublishing();
  }

  private void unregisterServletWhenNoresourcePresents() {
    if( !getRootApplication().hasResources() && isApplicationRegistered ) {
      // unregistering while jersey context is being reloaded can lead to many exceptions
      synchronized( servletContainerBridge ) {
        httpService.unregister( configuration.getRoothPath() );
        servletContainerBridge.reset();
        resourcePublisher.cancelPublishing();
        isApplicationRegistered = false;
      }
    }
  }

  public List<Object> eliminate() {
    if( isApplicationRegistered ) {
      // unregistering while jersey context is being reloaded can lead to many exceptions
      synchronized( servletContainerBridge ) {
        try {
          // this should call destroy on our servlet container
          httpService.unregister( configuration.getRoothPath() );
        } catch( Exception jerseyShutdownException ) {
          // do nothing because jersey sometimes throws an exception during shutdown
        }
      }
      resourcePublisher.cancelPublishing();
    }
    return new ArrayList<Object>( getRootApplication().getResources() );
  }

  // For testing purpose
  RootApplication getRootApplication() {
    return application;
  }

}
