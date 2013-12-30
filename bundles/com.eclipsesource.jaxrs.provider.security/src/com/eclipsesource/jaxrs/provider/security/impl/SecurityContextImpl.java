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

import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

public class SecurityContextImpl implements SecurityContext {

  private final boolean secure;
  private final String authenticationScheme;
  private final Principal principal;
  private final AuthorizationHandler authorizationHandler;

  public SecurityContextImpl( String authenticationScheme,
                              Principal principal,
                              boolean secure,
                              AuthorizationHandler authorizationHandler )
  {
    this.authenticationScheme = authenticationScheme;
    this.principal = principal;
    this.secure = secure;
    this.authorizationHandler = authorizationHandler;
  }

  @Override
  public String getAuthenticationScheme() {
    return authenticationScheme;
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isSecure() {
    return secure;
  }

  @Override
  public boolean isUserInRole( String role ) {
    if( authorizationHandler != null ) {
      return authorizationHandler.isUserInRole( principal, role );
    } 
    return true;
  }
}
