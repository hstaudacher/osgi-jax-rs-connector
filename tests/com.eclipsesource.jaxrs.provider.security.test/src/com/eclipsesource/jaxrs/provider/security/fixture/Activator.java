package com.eclipsesource.jaxrs.provider.security.fixture;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(TestResource.class, new TestResource(), null);
		
		AuthService authService = new AuthService();
		context.registerService(AuthenticationService.class, authService, null);
		context.registerService(AuthorizationService.class, authService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
