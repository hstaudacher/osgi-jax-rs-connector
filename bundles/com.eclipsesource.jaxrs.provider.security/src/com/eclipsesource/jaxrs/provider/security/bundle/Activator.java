package com.eclipsesource.jaxrs.provider.security.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.provider.security.RolesAllowedDynamicFeatureProvider;
import com.eclipsesource.jaxrs.security.AuthenticationService;
import com.eclipsesource.jaxrs.security.AuthorizationService;

public class Activator implements BundleActivator {

	private static Activator instance;
	private ServiceRegistration<RolesAllowedDynamicFeatureProvider> rolesAllowedDynamicFeatureProviderServiceRegistration;
	private ServiceTracker<AuthenticationService, AuthenticationService> authenticationServiceTracker;
	private ServiceTracker<AuthorizationService, AuthorizationService> authorizationServiceTracker;
	
	public static Activator getInstance()
	{
		return instance;
	}
	
	public AuthenticationService getAuthenticationService()
	{
		return authenticationServiceTracker.getService();
	}
	
	public AuthorizationService getAuthorizationService()
	{
		return authorizationServiceTracker.getService();
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		rolesAllowedDynamicFeatureProviderServiceRegistration = context.registerService(RolesAllowedDynamicFeatureProvider.class, new RolesAllowedDynamicFeatureProvider(), null);
		
		authenticationServiceTracker = new ServiceTracker<AuthenticationService, AuthenticationService>(context, AuthenticationService.class, null);
		authenticationServiceTracker.open();
		
		authorizationServiceTracker = new ServiceTracker<AuthorizationService, AuthorizationService>(context, AuthorizationService.class, null);
		authorizationServiceTracker.open();
		
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		
		if(rolesAllowedDynamicFeatureProviderServiceRegistration != null)
			rolesAllowedDynamicFeatureProviderServiceRegistration.unregister();
		
		if(authenticationServiceTracker != null)
			authenticationServiceTracker.close();
		
		if(authorizationServiceTracker != null)
			authorizationServiceTracker.close();
	}

}
