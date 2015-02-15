/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    Ivan Iliev - added ServletConfiguration tests
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import com.eclipsesource.jaxrs.publisher.ServletConfiguration;


@RunWith( MockitoJUnitRunner.class )
public class JAXRSConnector_Test {
  
  @Mock
  private ServiceReference httpServiceReference;
  @Mock
  private ServiceReference resourceServiceReference;
  @Mock
  private BundleContext bundleContext;
  @Mock
  private JerseyContext jerseyContext;
  
  private JAXRSConnector connector;

  @Before
  public void setUp() {
    JAXRSConnector original = new JAXRSConnector( bundleContext );
    connector = spy( original );
    doReturn( jerseyContext ).when( connector ).createJerseyContext( any( HttpService.class ), anyString(), eq( false ), anyLong(), any (ServletConfiguration.class));
  }
  
  @Test
  public void testAddHttpService() {
    HttpService httpService = mock( HttpService.class );
    when( bundleContext.getService( httpServiceReference ) ).thenReturn( httpService );
    
    HttpService httpService2 = connector.addHttpService( httpServiceReference );
    
    assertSame( httpService, httpService2 );
  }
  
  @Test
  public void testRegisterHttpServiceBeforeResource() {
    mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    
    connector.addHttpService( httpServiceReference );
    connector.addResource( resourceServiceReference );
    
    verify( jerseyContext ).addResource( resource );
  }
  
  @Test
  public void testUsesResourceCache() {
    HttpService httpService = mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    ArrayList<Object> resourceList = new ArrayList<Object>();
    resourceList.add( resource );
    when( jerseyContext.eliminate() ).thenReturn( resourceList );
    
    connector.addHttpService( httpServiceReference );
    connector.addResource( resourceServiceReference );
    connector.removeHttpService( httpService );
    connector.addHttpService( httpServiceReference );
    
    verify( jerseyContext, times( 2 ) ).addResource( resource );
  }
  
  @Test
  public void testRegisterHttpServiceAfterResource() {
    mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    
    connector.addResource( resourceServiceReference );
    connector.addHttpService( httpServiceReference );
    
    verify( jerseyContext ).addResource( resource );
  }
  
  @Test
  public void testUpdatePath() {
    mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    
    connector.addResource( resourceServiceReference );
    connector.addHttpService( httpServiceReference );
    connector.updateConfiguration( "/test", false, 23 );
    
    InOrder order = inOrder( connector );
    order.verify( connector ).createJerseyContext( any( HttpService.class ), anyString(), eq( false ), eq( 150L ), any(ServletConfiguration.class) );
    order.verify( connector ).doRemoveHttpService( any( HttpService.class ) );
    order.verify( connector ).doAddHttpService( any( ServiceReference.class ) );
    order.verify( connector ).createJerseyContext( any( HttpService.class ), eq( "/test" ), eq( false ), eq( 23L ), any(ServletConfiguration.class) );
  }
  
  @Test
  public void testRemoveHttpService() {
    HttpService httpService = mockHttpService();
    connector.addHttpService( httpServiceReference );
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    connector.addResource( resourceServiceReference );
    
    connector.removeHttpService( httpService );
    
    verify( jerseyContext ).eliminate();
  }
  
  @Test
  public void testRemoveResource() {
    mockHttpService();
    connector.addHttpService( httpServiceReference );
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    connector.addResource( resourceServiceReference );
    
    connector.removeResource( resource );
    
    verify( jerseyContext ).removeResource( resource );
  }

  @Test
  public void testRegisterResourceWithoutHttpService() {
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    
    connector.addResource( resourceServiceReference );
    
    verify( jerseyContext, never() ).addResource( resource );
  }
  
  @Test
  public void testRegisterOnlyWithRightPort() {
    mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    when( resourceServiceReference.getProperty( anyString() ) ).thenReturn( "9090" );
    
    connector.addHttpService( httpServiceReference );
    connector.addResource( resourceServiceReference );
    
    verify( jerseyContext, never() ).addResource( resource );
  }
  
  @Test
  public void testServletConfigurationUpdatesHttpServices() {
    HttpService mockHttpService = mockHttpService();
    Object resource = new Object();
    when( bundleContext.getService( resourceServiceReference ) ).thenReturn( resource );
    when( resourceServiceReference.getProperty( anyString() ) ).thenReturn( "80" );
    
    connector.addHttpService( httpServiceReference );
    connector.addResource( resourceServiceReference );
    
    ServletConfiguration servletConfiguration = mock( ServletConfiguration.class );
    ServiceReference servletConfigurationReference = mock( ServiceReference.class );
    
    when( bundleContext.getService( servletConfigurationReference ) ).thenReturn( servletConfiguration );
    
    connector.setServletConfiguration( servletConfigurationReference );
    // adding a configuration service will remove and read all http services
    verify( connector, times( 1 ) ).doRemoveHttpService( mockHttpService );
    verify( connector, times( 2 ) ).doAddHttpService( httpServiceReference );
    
    connector.unsetServletConfiguration( servletConfigurationReference, servletConfiguration );

    // removing a configuration service will remove and read all http services
    verify( connector, times( 2 ) ).doRemoveHttpService( mockHttpService );
    verify( connector, times( 3 ) ).doAddHttpService( httpServiceReference );
  }
  
  @Test
  public void testServletConfigurationWithoutHttpServices() {
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    ServiceReference servletConfigurationServiceReference = mock( ServiceReference.class );
    
    when( bundleContext.getService( servletConfigurationServiceReference ) ).thenReturn( servletConfigurationService );
    
    connector.setServletConfiguration( servletConfigurationServiceReference );
    
    verify( connector, never() ).doRemoveHttpService( any( HttpService.class ) );
    verify( connector, never() ).doAddHttpService( any( ServiceReference.class ) );
    
    connector.unsetServletConfiguration( servletConfigurationServiceReference, servletConfigurationService );
    
    verify( connector, never() ).doRemoveHttpService( any( HttpService.class ) );
    verify( connector, never() ).doAddHttpService( any( ServiceReference.class ) );
  }

  private HttpService mockHttpService() {
    HttpService httpService = mock( HttpService.class );
    when( bundleContext.getService( httpServiceReference ) ).thenReturn( httpService );
    when( httpServiceReference.getProperty( anyString() ) ).thenReturn( "80" );
    return httpService;
  }
}
