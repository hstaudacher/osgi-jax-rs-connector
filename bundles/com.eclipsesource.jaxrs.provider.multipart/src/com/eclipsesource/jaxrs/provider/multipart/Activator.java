/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.multipart;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    registration = bundleContext.registerService( MultiPartFeature.class.getName(), new MultiPartFeature(), null );
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    if( registration != null ) {
      registration.unregister();
    }
  }
}
