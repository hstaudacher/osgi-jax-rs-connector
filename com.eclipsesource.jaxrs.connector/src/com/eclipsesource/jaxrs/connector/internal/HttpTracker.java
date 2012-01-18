/*******************************************************************************
 * Copyright (c) 2011 Frank Appel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Frank Appel - initial API and implementation
 *    Holger Staudacher - ongoing development
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.internal;


import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;


class HttpTracker extends ServiceTracker<HttpService, HttpService> {

  private final JAXRSConnector connector;

  HttpTracker( BundleContext context, JAXRSConnector connector ) {
    super( context, HttpService.class.getName(), null );
    this.connector = connector;
  }

  @Override
  public HttpService addingService( ServiceReference<HttpService> reference ) {
    return connector.addHttpService( reference );
  }

  @Override
  public void removedService( ServiceReference<HttpService> reference, HttpService service ) {
    connector.removeHttpService( service );
  }
}
