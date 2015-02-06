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
package com.eclipsesource.jaxrs.publisher.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.publisher.ServletConfigurationService;


@RunWith( MockitoJUnitRunner.class )
public class ServiceConfigurationTracker_Test {
  
  private ServletConfigurationTracker servletConfigurationTracker;
  
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference reference;

  @Before
  public void setUp() {
    BundleContext context = mock( BundleContext.class );
    servletConfigurationTracker = new ServletConfigurationTracker( context, connector );
  }
  
  @Test
  public void delegatesAddServletConfigurationService() {
    servletConfigurationTracker.addingService( reference );
    
    verify( connector ).setServletConfiguration( reference );
  }
  
  @Test
  public void delegatesRemoveServletConfigurationService() {
    ServletConfigurationService service = mock( ServletConfigurationService.class );
    
    servletConfigurationTracker.removedService( reference, service );
    
    verify( connector ).unsetServletConfiguration( reference, service );
  }
}
