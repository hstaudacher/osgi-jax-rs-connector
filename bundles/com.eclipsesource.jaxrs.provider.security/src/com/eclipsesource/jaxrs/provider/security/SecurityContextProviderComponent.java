/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt. All rights reserved. This program and the 
 * accompanying materials are made available under the terms of the Eclipse Public 
 * License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Bryan Hunt - initial API and implementation
 *******************************************************************************/

package com.eclipsesource.jaxrs.provider.security;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.provider.security.bundle.Activator;
import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;
import com.eclipsesource.jaxrs.security.SecurityContextProvider;

public class SecurityContextProviderComponent implements SecurityContextProvider {

    @Override
    public SecurityContext getSecurityContext(ContainerRequestContext requestContext)
    {
        AuthenticationService authenticationService = Activator.getInstance().getAuthenticationService();
        AuthorizationService authorizationService = Activator.getInstance().getAuthorizationService();

        if(authenticationService == null || authorizationService == null)
        	return null;
        
        Principal principal = authenticationService.authenticate(requestContext);

    	if (principal == null)
    		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());

    	return new AuthSecurityContext(authenticationService.getAuthenticationScheme(), principal, requestContext.getUriInfo().getRequestUri().getScheme().equals("https"), authorizationService);
    }
}
