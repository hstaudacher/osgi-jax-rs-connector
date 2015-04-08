/*******************************************************************************
 * Copyright (c) 2014,2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import javax.ws.rs.core.Request;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class ServletContainerBridge implements Runnable {
  
  private final RootApplication application;
  private ServletContainer servletContainer;

  public ServletContainerBridge( RootApplication application ) {
    this.servletContainer = new ServletContainer( ResourceConfig.forApplication( application ) );
    this.application = application;
  }

  @Override
  public void run() {
    if( application.isDirty() ) {
      ClassLoader original = Thread.currentThread().getContextClassLoader();
      try {
        Thread.currentThread().setContextClassLoader( Request.class.getClassLoader() );
        synchronized( this ) {
          getServletContainer().reload( ResourceConfig.forApplication( application ) );
        }
      } finally {
        Thread.currentThread().setContextClassLoader( original );
      }
    }
  }
  
  public ServletContainer getServletContainer() {
    return servletContainer;
  }

  public void destroy() {
    synchronized( this ) {
      servletContainer.destroy();
    }
  }

  public void reset() {
    synchronized( this ) {
      this.servletContainer = new ServletContainer( ResourceConfig.forApplication( application ) );
    }
  }
}
