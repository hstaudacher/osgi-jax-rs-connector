package com.eclipsesource.jaxrs.security.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.security.SecurityContextProvider;

public class Activator implements BundleActivator {

	private static Activator instance;
	private ServiceTracker<SecurityContextProvider, SecurityContextProvider> securityContentProviderTracker;
	
	public static Activator getInstance()
	{
		return instance;
	}
	
	public SecurityContextProvider getSecurityContextProvider()
	{
		return securityContentProviderTracker.getService();
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		securityContentProviderTracker = new ServiceTracker<SecurityContextProvider, SecurityContextProvider>(context, SecurityContextProvider.class, null);
		securityContentProviderTracker.open();
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		
		if(securityContentProviderTracker != null)
			securityContentProviderTracker.close();
	}
}
