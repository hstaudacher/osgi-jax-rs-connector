/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt. All rights reserved. This program and the 
 * accompanying materials are made available under the terms of the Eclipse Public 
 * License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Bryan Hunt - initial API and implementation
 *******************************************************************************/

package com.eclipsesource.jaxrs.security.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.eclipsesource.jaxrs.security.SecurityContextProvider;
import com.eclipsesource.jaxrs.security.bundle.Activator;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerRequestFilterProvider implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		SecurityContextProvider securityContextProvider = Activator.getInstance().getSecurityContextProvider();
		
		if(securityContextProvider != null)
		{
			SecurityContext securityContext = securityContextProvider.getSecurityContext(requestContext);
			
			if(securityContext != null)
				requestContext.setSecurityContext(securityContext);
		}
	}
}
