package com.eclipsesource.jaxrs.sample.service.impl;

import com.eclipsesource.jaxrs.sample.service.GreetingResource;
import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

@Slf4j
public class Activator implements BundleActivator {

    private ServiceRegistration<GreetingResource> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        log.info("Registering service {}", GreetingResource.class);
        registration = context.registerService(GreetingResource.class, new SimpleResource(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        log.info("Un-registering service {}", GreetingResource.class);
        registration.unregister();
    }
}
