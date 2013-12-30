/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Holger Staudacher - API finalization
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security;


import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

/**
 * <p>
 * The {@link AuthenticationHandler} is part of the security provider and needs to be registered as an OSGi servide. 
 * Client may implement this interface to perform user authentication and to define an authentication scheme. 
 * These values will be used later on in a {@link SecurityContext} that is used by the framework to verify 
 * authentication.
 * </p>
 * <p>
 * An {@link AuthenticationHandler} may be used together with an {@link AuthorizationHandler}.
 * </p>
 * 
 * @see SecurityContext
 * @see AuthorizationHandler
 */
public interface AuthenticationHandler {

  /**
   * <p>
   * Authenticates the user initiating the request. Implementations must return null if the
   * user could not be authenticated.
   * </p>
   * 
   * @param requestContext the request context supplied by the underlying JAX-RS implementation.
   * @return a principal representing the authenticated user initiating the request or {@code null} if
   *         the user could not be authenticated.
   */
  Principal authenticate( ContainerRequestContext requestContext );

  /**
   * @return the authentication scheme. Must be one of {@link SecurityContext.BASIC_AUTH},
   *         {@link SecurityContext.CLIENT_CERT_AUTH,} {@link SecurityContext.DIGEST_AUTH},
   *         {@link SecurityContext.FORM_AUTH} or {@code null}.
   *         
   * @see SecurityContext
   */
  String getAuthenticationScheme();
}
