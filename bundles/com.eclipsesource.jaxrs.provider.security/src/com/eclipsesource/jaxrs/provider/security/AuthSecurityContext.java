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

import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.security.AuthorizationService;

public class AuthSecurityContext implements SecurityContext {
	private boolean secure;
	private String authenticationScheme;
	private Principal principal;
	private AuthorizationService authorizationService;

	public AuthSecurityContext(String authenticationScheme, Principal principal, boolean secure, AuthorizationService authorizationService) {
		this.authenticationScheme = authenticationScheme;
		this.principal = principal;
		this.secure = secure;
		this.authorizationService = authorizationService;
	}

	@Override
	public String getAuthenticationScheme() {
		return authenticationScheme;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public boolean isUserInRole(String role) {
		return authorizationService.isUserInRole(principal, role);
	}
}
