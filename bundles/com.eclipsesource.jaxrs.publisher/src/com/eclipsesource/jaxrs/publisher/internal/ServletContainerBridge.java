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
        getServletContainer().reload( ResourceConfig.forApplication( application ) );
      } finally {
        Thread.currentThread().setContextClassLoader( original );
        application.setDirty( false );
      }
    }
  }
  
  public ServletContainer getServletContainer() {
    return servletContainer;
  }

  public void destroy() {
    servletContainer.destroy();
  }

  public void reset() {
    this.servletContainer = new ServletContainer( ResourceConfig.forApplication( application ) );
  }
}
