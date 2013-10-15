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

public interface SecurityContextProvider {
	SecurityContext getSecurityContext(ContainerRequestContext requestContext);
}