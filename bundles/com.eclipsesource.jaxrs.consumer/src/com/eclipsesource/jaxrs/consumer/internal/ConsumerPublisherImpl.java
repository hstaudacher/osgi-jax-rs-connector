/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 *    Frank Appel - specified property to exclude resources from publishing
 *    ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class ConsumerPublisherImpl implements ConsumerPublisher {

  private final BundleContext context;
  private final List<ServiceRegistration> registrations;

  public ConsumerPublisherImpl( BundleContext context ) {
    this.registrations = new ArrayList<ServiceRegistration>();
    this.context = context;
  }

  @Override
  public void publishConsumers( String baseUrl, Class<?>[] types, Object[] providers ) {
    for( Class<?> type : types ) {
      ClientConfig config = new ClientConfig();
      registerProviders( config, providers );
      Object resource = ConsumerFactory.createConsumer( baseUrl, config, type );
      Dictionary<String, Object> properties = new Hashtable<String, Object>();
      properties.put( "com.eclipsesource.jaxrs.publish", "false" );
      ServiceRegistration registration = context.registerService( type.getName(), resource, properties );
      registrations.add( registration );
    }
  }

  private void registerProviders( ClientConfig config, Object[] providers ) {
    if( providers != null ) {
      for( Object provider : providers ) {
        config.register( provider );
      }
    }
  }

  public void unregister() {
    for( ServiceRegistration registration : registrations ) {
      registration.unregister();
    }
  }
}
