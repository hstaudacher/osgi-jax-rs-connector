package com.eclipsesource.jaxrs.provider.security.fixture;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class AuthService implements AuthenticationService, AuthorizationService {

	@Override
	public boolean isUserInRole(Principal user, String role) {
		return user.getName().equals("junit") && role.equals("secure");
	}

	@Override
	public Principal authenticate(ContainerRequestContext requestContext) {
		Cookie userCookie = requestContext.getCookies().get("user");
		return userCookie != null ? new User(userCookie.getValue()) : null;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.FORM_AUTH;
	}
}
