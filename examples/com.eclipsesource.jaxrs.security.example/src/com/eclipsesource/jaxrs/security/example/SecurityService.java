package com.eclipsesource.jaxrs.security.example;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class SecurityService implements AuthenticationService, AuthorizationService {

	// This is a simple example of a security service that uses cookies for authentication.
	// The client request header must include a "user=junit" and "auth=junit" cookie for
	// authentication to succeed.  The authorization is successful only if the user is
	// "junit" and the role is "secure".
	
	@Override
	public boolean isUserInRole(Principal user, String role) {
		return user.getName().equals("junit") && role.equals("secure");
	}

	@Override
	public Principal authenticate(ContainerRequestContext requestContext) {
		Cookie userCookie = requestContext.getCookies().get("user");
		Cookie authCookie = requestContext.getCookies().get("auth");
		
		if(userCookie == null || authCookie == null || !userCookie.getValue().equals(authCookie.getValue()))
			return null;
		
		return new User(userCookie.getValue()); 
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.FORM_AUTH;
	}
}
