/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer.internal;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class ConsumerPublisherImpl implements ConsumerPublisher {

  private final BundleContext context;
  private final List<ServiceRegistration<?>> registrations;

  public ConsumerPublisherImpl( BundleContext context ) {
    this.registrations = new ArrayList<ServiceRegistration<?>>();
    this.context = context;
  }

  @Override
  public void publishConsumers( String baseUrl, Class<?>[] types, Object[] providers ) {
    for( Class<?> type : types ) {
      Object resource = ConsumerFactory.createConsumer( baseUrl, type, providers );
      ServiceRegistration<?> registration = context.registerService( type.getName(), resource, null );
      registrations.add( registration );
    }
  }

  public void unregister() {
    for( ServiceRegistration<?> registration : registrations ) {
      registration.unregister();
    }
  }
}
