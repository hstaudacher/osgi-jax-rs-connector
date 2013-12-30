/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.security.example;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

/**
 * This is a simple example of a security handler that uses headers for authentication.
 * The client request header must include a "user=test" for authentication to succeed. 
 * The authorization is successful only if the user is "test" and the role is "secure".
 * 
 * @see SecureResource
 */
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {

  @Override
  public boolean isUserInRole( Principal user, String role ) {
    return user.getName().equals( "test" ) && role.equals( "secure" );
  }

  @Override
  public Principal authenticate( ContainerRequestContext requestContext ) {
    String user = requestContext.getHeaderString( "user" );
    if( user == null ) {
      return null;
    }
    return new User( user );
  }

  @Override
  public String getAuthenticationScheme() {
    return null;
  }
}
