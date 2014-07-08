package com.eclipsesource.jaxrs.connector.example.mvc;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.mvc.MvcFeature;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private List<ServiceRegistration> registrations;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    registrations = new ArrayList<>();
    registrations.add( bundleContext.registerService( MvcFeature.class.getName(), new MvcFeature(), null ) );
    registrations.add( bundleContext.registerService( MustacheTemplateProcessor.class.getName(), new MustacheTemplateProcessor(), null ) );
    registrations.add( bundleContext.registerService( Static.class.getName(), new Static(), null ) );
    registrations.add( bundleContext.registerService( Index.class.getName(), new Index(), null ) );
    
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    for( int i = 0; i < registrations.size(); i++ ) {
      registrations.get( i ).unregister();
    }
  }
}
