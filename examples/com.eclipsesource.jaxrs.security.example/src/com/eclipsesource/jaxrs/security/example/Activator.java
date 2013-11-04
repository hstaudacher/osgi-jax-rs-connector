package com.eclipsesource.jaxrs.security.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class Activator implements BundleActivator {

	public void start(BundleContext bundleContext) throws Exception {
		bundleContext.registerService(TestResource.class, new TestResource(), null);
		
		SecurityService securityService = new SecurityService();
		bundleContext.registerService(AuthenticationService.class, securityService, null);
		bundleContext.registerService(AuthorizationService.class, securityService, null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	}
}
