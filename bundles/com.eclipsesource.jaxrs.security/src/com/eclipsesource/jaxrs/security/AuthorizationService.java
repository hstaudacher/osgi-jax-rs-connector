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

/**
 * This is an OSGi service interface for authorizing users requesting access
 * to a RESTful service.
 * 
 * @author bhunt
 */
public interface AuthorizationService {
	
	/**
	 * Determines whether or not the requesting user is in the specified role.
	 * 
	 * @param user the user requesting access
	 * @param role the role protecting the access
	 * @return true if the user is in the role; false otherwise
	 */
	boolean isUserInRole(Principal user, String role);
}
