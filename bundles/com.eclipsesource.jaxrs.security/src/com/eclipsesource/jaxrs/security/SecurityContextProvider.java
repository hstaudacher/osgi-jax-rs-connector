/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt. All rights reserved. This program and the 
 * accompanying materials are made available under the terms of the Eclipse Public 
 * License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Bryan Hunt - initial API and implementation
 *******************************************************************************/

package com.eclipsesource.jaxrs.security;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

/**
 * This is an OSGi service interface for providing a security context for
 * accesses to a RESTful service.
 * 
 * @author bhunt
 */
public interface SecurityContextProvider {
	
	/**
	 * @param requestContext the request context provided by Jerset for the incoming RESTful request
	 * @return the security context used to protect the resource.
	 */
	SecurityContext getSecurityContext(ContainerRequestContext requestContext);
}
