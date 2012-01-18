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

import java.util.List;

import javax.servlet.ServletException;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.sun.jersey.spi.container.servlet.ServletContainer;


public class JerseyContext {

  static final String APPLICATION_ROOT = "/";

  private final RootApplication rootApplication;
  private final ServletContainer servletContainer;
  private final HttpService httpService;
  private boolean isApplicationRegistered;

  public JerseyContext( HttpService httpService ) {
    this.httpService = httpService;
    this.rootApplication = new RootApplication();
    this.servletContainer = new ServletContainer( rootApplication );
  }

  public void addResource( Object resource ) {
    getRootApplication().addResource( resource );
    registerServletWhenNotAlreadyRegistered();
    if( isApplicationRegistered ) {
      getServletContainer().reload();
    }
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
    httpService.registerServlet( APPLICATION_ROOT, 
                                 getServletContainer(), 
                                 null, 
                                 null );
  }

  private void resetContextClassloader( ClassLoader loader ) {
    Thread.currentThread().setContextClassLoader( loader );
  }
  
  public void removeResource( Object resource ) {
    getRootApplication().removeResource( resource );
    if( isApplicationRegistered ) {
      getServletContainer().reload();
    }
    unregisterServletWhenNoresourcePresents();
  }

  private void unregisterServletWhenNoresourcePresents() {
    if( !getRootApplication().hasResources() ) {
      httpService.unregister( APPLICATION_ROOT );
      isApplicationRegistered = false;
    }
  }

  public List<Object> eliminate() {
    getServletContainer().destroy();
    httpService.unregister( APPLICATION_ROOT );
    return getRootApplication().getResources();
  }

  // For testing purpose only
  ServletContainer getServletContainer() {
    return servletContainer;
  }
  
  // For testing purpose only
  RootApplication getRootApplication() {
    return rootApplication;
  }

}
