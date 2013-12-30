/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Holger Staudacher - API finalization
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

public class SecurityAdmin {

  public SecurityContext getSecurityContext( ContainerRequestContext requestContext ) {
    SecurityContext result = null;
    AuthenticationHandler authenticationHandler = Activator.getInstance().getAuthenticationHandler();
    AuthorizationHandler authorizationHandler = Activator.getInstance().getAuthorizationHandler();
    if( authenticationHandler != null && authorizationHandler != null ) {
      result = createSecurityContext( requestContext, authenticationHandler, authorizationHandler );
    }
    return result;
  }

  private SecurityContext createSecurityContext( ContainerRequestContext requestContext,
                                                 AuthenticationHandler authenticationHandler,
                                                 AuthorizationHandler authorizationHandler )
  {
    Principal principal = authenticationHandler.authenticate( requestContext );
    if( principal != null ) {
      return new SecurityContextImpl( authenticationHandler.getAuthenticationScheme(),
                                      principal,
                                      requestContext.getUriInfo().getRequestUri().getScheme().equals( "https" ),
                                      authorizationHandler );
    }
    return null;
  }
  
}
