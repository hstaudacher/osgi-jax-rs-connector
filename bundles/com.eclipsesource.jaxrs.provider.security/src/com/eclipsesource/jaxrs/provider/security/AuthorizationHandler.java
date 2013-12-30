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

/**
 * <p>
 * The {@link AuthorizationHandler} is part of the security provider and needs to be registered as an OSGi service.
 * Clients may implement the {@link AuthorizationHandler#isUserInRole(Principal, String)} method to authorize a 
 * specific user. The {@link Principal} used to call this method will be taken from the {@link AuthenticationHandler}. 
 * </p>
 * 
 * @see AuthenticationHandler
 */
public interface AuthorizationHandler {

  /**
   * <p>
   * Determines whether or not the requesting user is in the specified role.
   * </p>
   * 
   * @param user the user requesting access.
   * @param role the role protecting the access.
   * 
   * @return {@code true} if the user is in the role or {@code false} otherwise
   * 
   * @see AuthenticationHandler
   */
  boolean isUserInRole( Principal user, String role );
  
}
