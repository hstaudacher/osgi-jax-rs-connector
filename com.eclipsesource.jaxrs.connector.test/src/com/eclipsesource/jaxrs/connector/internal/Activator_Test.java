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
package com.eclipsesource.jaxrs.connector.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;


@RunWith( MockitoJUnitRunner.class )
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class Activator_Test {
  
  private Activator activator;
  @Mock
  private BundleContext context;
  @Mock
  private ServiceRegistration registration;
  @Mock
  private Bundle jerseyServer;

  @Before
  public void setUp() throws InvalidSyntaxException {
    Activator original = new Activator();
    activator = spy( original );
    doReturn( jerseyServer ).when( activator ).getJerseyServerBundle();
    Filter filter = mock( Filter.class );
    when( context.createFilter( anyString() ) ).thenReturn( filter );
    when( context.registerService( anyString(), 
                                   anyObject(), 
                                   any( Dictionary.class ) ) ).thenReturn( registration );
  }
  
  @Test
  public void testRegisterService() throws Exception {
    
    activator.start( context );
    
    verify( context ).registerService( eq( JAXRSConnector.class.getName() ), 
                                       any( JAXRSConnector.class ), 
                                       any( Dictionary.class ) );
  }
  
  @Test
  public void testDeregisterService() throws Exception {
    activator.start( context );
    
    activator.stop( context );
    
    verify( registration ).unregister();
  }
  
  @Test
  public void testStartsJerseyServer() throws Exception {
    when( jerseyServer.getState() ).thenReturn( Bundle.INSTALLED );
    activator.start( context );
    
    verify( jerseyServer ).start();
  }
  
  @Test
  public void testStartsNotJerseyServer() throws Exception {
    when( jerseyServer.getState() ).thenReturn( Bundle.ACTIVE );
    activator.start( context );
    
    verify( jerseyServer, never() ).start();
  }
}
