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
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class SecurityService implements AuthenticationService, AuthorizationService {

  // This is a simple example of a security service that uses headers for authentication.
  // The client request header must include a "user=junit" and "auth=junit" value for
  // authentication to succeed. The authorization is successful only if the user is
  // "junit" and the role is "secure".
  @Override
  public boolean isUserInRole( Principal user, String role ) {
    return user.getName().equals( "junit" ) && role.equals( "secure" );
  }

  @Override
  public Principal authenticate( ContainerRequestContext requestContext ) {
    String user = requestContext.getHeaderString( "user" );
    String auth = requestContext.getHeaderString( "auth" );
    if( user == null || auth == null || !user.equals( auth ) ) {
      return null;
    }
    return new User( user );
  }

  @Override
  public String getAuthenticationScheme() {
    return SecurityContext.FORM_AUTH;
  }
}
