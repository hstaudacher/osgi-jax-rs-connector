/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt. All rights reserved. This program and the 
 * accompanying materials are made available under the terms of the Eclipse Public 
 * License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Bryan Hunt - initial API and implementation
 *******************************************************************************/

package com.eclipsesource.jaxrs.security;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * This is an OSGi service interface for authenticating users requesting access
 * to a RESTful service.
 * 
 * @author bhunt
 */
public interface AuthenticationService {
	
	/**
	 * Authenticates the user initiating the RESTful request.  Implementations
	 * must return null if the user could not be authenticated.
	 * 
	 * @param requestContext the request context supplied by Jersey
	 * @return a principal representing the authenticated user initiating the RESTful request;
	 *         null if the user could not be authenticated
	 */
	Principal authenticate(ContainerRequestContext requestContext);

	/**
	 * 
	 * @return the authentication scheme.  Must be one of SecurityContext.BASIC_AUTH,
	 *         SecurityContext.CLIENT_CERT_AUTH, SecurityContext.DIGEST_AUTH, or
	 *         SecurityContext.FORM_AUTH
	 */
	String getAuthenticationScheme();
}
