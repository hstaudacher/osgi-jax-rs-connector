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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;
  private ConsumerPublisherImpl publisher;

  @Override
  public void start( BundleContext context ) throws Exception {
    publisher = new ConsumerPublisherImpl( context );
    registration = context.registerService( ConsumerPublisher.class.getName(), 
                                            publisher,
                                            null );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    publisher.unregister();
    registration.unregister();
  }
}
