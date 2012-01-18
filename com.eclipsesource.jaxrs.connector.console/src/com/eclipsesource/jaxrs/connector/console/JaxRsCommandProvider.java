/*******************************************************************************
 * Copyright (c) 2011, 2012 Frank Appel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Frank Appel - initial API and implementation
 *     Holger Staudacher - ongoing development
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.console;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.equinox.http.jetty.JettyConstants;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;


public class JaxRsCommandProvider implements CommandProvider {

  private static final String HTTP_SERVER_MANAGER_ID = "org.eclipse.equinox.http.jetty.config";

  private BundleContext bundleContext;
  
  
  public JaxRsCommandProvider( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
  }
  
  public void _sh( CommandInterpreter commandInterpreter ) {
    String port = getPort( commandInterpreter );
    if( null != port ) {
      startHttpService( commandInterpreter, port );
    }
  }

  public void _hh( CommandInterpreter commandInterpreter ) {
    String port = getPort( commandInterpreter );
    if( null != port ) {
      stopHttpService( commandInterpreter, port );
    }
  }
  
  public void _ds( CommandInterpreter commandInterpreter ) {
    String serviceClass = getServiceName( commandInterpreter );
    String port = getPort( commandInterpreter );
    if( null != port ) {
      ConfigurationAdmin configurationAdmin = getConfigurationAdmin();
      try {
        Configuration configuration = configurationAdmin.createFactoryConfiguration( serviceClass );
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put( "http.port", port );
        configuration.update( properties );
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getHelp() {
    return   "---Configuration of ApplicationLauncher---\n"
           + "\tstartHttpService (sh) <port>\n"
           + "\tstopHttpService (hh) <port>\n";
  }
  
  private String getPort( CommandInterpreter commandInterpreter ) {
    return getArgument( commandInterpreter, "Parameter port must not be null" );
  }
  
  private String getServiceName( CommandInterpreter commandInterpreter ) {
    return getArgument( commandInterpreter, "Parameter port must not be null" );
  }
  
  private String getArgument( CommandInterpreter commandInterpreter, String message ) {
    String result = commandInterpreter.nextArgument();
    if( result == null && message != null ) {
      commandInterpreter.println( message );
    }
    return result;
  }

  
  private String createPortFilter( String port ) {
    return "(" + JettyConstants.HTTP_PORT + "=" + port + ")";
  }

  private ConfigurationAdmin getConfigurationAdmin() {
    Class<ConfigurationAdmin> type = ConfigurationAdmin.class;
    ServiceReference<ConfigurationAdmin> reference = bundleContext.getServiceReference( type );
    return bundleContext.getService( reference );
  }

  private void startHttpService( CommandInterpreter commandInterpreter,
                                 String port )
  {
    try {
      Configuration configuration = createHttpServiceConfiguration();
      configuration.update( createHttpServiceSettings( port ) );
    } catch( IOException ioe ) {
      commandInterpreter.println( "Unable to start HttpService at port: " + port );
      commandInterpreter.println( ioe.getMessage() );
    }
  }

  private Dictionary<String, Object> createHttpServiceSettings( String port )
  {
    Dictionary<String,Object> result = new Hashtable<String, Object>();
    result.put( JettyConstants.HTTP_PORT, Integer.valueOf( port ) );
    return result;
  }

  private void stopHttpService( CommandInterpreter commandInterpreter, String port ) {
    try {
      ConfigurationAdmin configurationAdmin = getConfigurationAdmin();
      String filter = createPortFilter( port );
      Configuration[] configurations = configurationAdmin.listConfigurations( filter );
      if( configurations.length >= 1  ) {
        configurations[ 0 ].delete();
      }
    } catch( Exception exception ) {
      commandInterpreter.println( "Unable to stop HttpService at port: " + port );
      commandInterpreter.println( exception.getMessage() );
    }
  }

  private Configuration createHttpServiceConfiguration() throws IOException {
    ConfigurationAdmin configurationAdmin = getConfigurationAdmin();
    String location = findHttpServiceManagerLocation();
    return configurationAdmin.createFactoryConfiguration( HTTP_SERVER_MANAGER_ID, location );
  }

  private String findHttpServiceManagerLocation() {
    Bundle[] bundles = bundleContext.getBundles();
    String result = null;
    for( Bundle bundle : bundles ) {
      if( bundle.getSymbolicName().equals( "org.eclipse.equinox.http.jetty" ) ) {
        result = bundle.getLocation();
      }
    }
    return result;
  }
}
