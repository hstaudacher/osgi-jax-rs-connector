/*******************************************************************************
 * Copyright (c) 2012,2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    Ivan Iliev - added tests for ServletConfigurationTracker
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.eclipsesource.jaxrs.publisher.ApplicationConfiguration;
import com.eclipsesource.jaxrs.publisher.ServletConfiguration;


@RunWith( MockitoJUnitRunner.class )
public class JerseyContext_Test {

  private JerseyContext jerseyContext;
  @Mock
  private ServletContainer servletContainer;
  @Mock
  private HttpService httpService;
  @Mock
  private RootApplication rootApplication;
  @Mock
  private ServletContainerBridge servletContainerBridge;

  @Before
  public void setUp() {
    Configuration configuration = createConfiguration( "/test", false, 23L );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    JerseyContext original = new JerseyContext( httpService,
                                                configuration,
                                                servletConfigurationService,
                                                new ServiceContainer( mock( BundleContext.class ) ),
                                                rootApplication,
                                                servletContainerBridge);
    jerseyContext = spy( original );
  }

  @Test
  public void testAddResource() throws ServletException, NamespaceException {
    Object resource = new Object();

    jerseyContext.addResource( resource );

    verify( rootApplication ).addResource( resource );
    verify( httpService ).registerServlet( eq( "/test" ), any( ServletContainer.class ), any( Dictionary.class ), any( HttpContext.class ) );
  }

  @Test
  public void testRemoveSingleResource() throws ServletException, NamespaceException {
    Object resource = new Object();

    jerseyContext.addResource( resource );
    jerseyContext.removeResource( resource );


    verify( rootApplication ).addResource( resource );
    verify( rootApplication ).removeResource( resource );
    verify( httpService ).registerServlet( eq( "/test" ), any( ServletContainer.class ), any( Dictionary.class ), any( HttpContext.class ) );
    verify( httpService ).unregister( "/test" );
  }

  @Test
  public void testRemoveResource() throws ServletException, NamespaceException {
    when( rootApplication.hasResources() ).thenReturn( true );
    Object resource = new Object();
    Object resource2 = new Object();

    jerseyContext.addResource( resource );
    jerseyContext.addResource( resource2 );
    jerseyContext.removeResource( resource );


    verify( rootApplication ).addResource( resource );
    verify( rootApplication ).addResource( resource2 );
    verify( rootApplication ).removeResource( resource );
    verify( httpService ).registerServlet( eq( "/test" ), any( ServletContainer.class ), any( Dictionary.class ), any( HttpContext.class ) );
    verify( httpService, never() ).unregister( "/test" );
  }

  @Test
  public void testEliminate() throws ServletException, NamespaceException {
    Object resource = new Object();
    Set<Object> list = new HashSet<Object>();
    list.add( resource );
    when( rootApplication.getResources() ).thenReturn( list );
    jerseyContext.addResource( resource );

    List<Object> resources = jerseyContext.eliminate();

    verify( rootApplication ).addResource( resource );
    verify( httpService ).unregister( "/test" );
    assertEquals( 1, resources.size() );
    assertEquals( resource, resources.get( 0 ) );
  }

  @Test
  public void testEliminateDoesNotFailWithException() throws ServletException, NamespaceException {
    doThrow( Exception.class ).when( httpService ).unregister( anyString() );
    Object resource = new Object();
    Set<Object> list = new HashSet<Object>();
    list.add( resource );
    when( rootApplication.getResources() ).thenReturn( list );
    jerseyContext.addResource( resource );

    List<Object> resources = jerseyContext.eliminate();

    verify( rootApplication ).addResource( resource );
    verify( httpService ).unregister( "/test" );
    assertEquals( 1, resources.size() );
    assertEquals( resource, resources.get( 0 ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testConvertsServletException() throws ServletException, NamespaceException {
    doThrow( new ServletException() ).when( httpService ).registerServlet( anyString(),
                                                                           any( Servlet.class ),
                                                                           any( Dictionary.class ),
                                                                           any( HttpContext.class ) );

    jerseyContext.addResource( new Object() );
  }

  @Test( expected = IllegalStateException.class )
  public void testConvertsNamespaceException() throws ServletException, NamespaceException {
    doThrow( new NamespaceException( "test" ) )
      .when( httpService ).registerServlet( anyString(),
                                            any( Servlet.class ),
                                            any( Dictionary.class ),
                                            any( HttpContext.class ) );

    jerseyContext.addResource( new Object() );
  }

  @Test
  public void testDoesNotRegster_METAINF_SERVICES_LOOKUP_DISABLE() {
    Configuration configuration = createConfiguration( "/test", false, 23L );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    JerseyContext context = new JerseyContext( httpService,
                                               configuration,
                                               servletConfigurationService,
                                               new ServiceContainer( mock( BundleContext.class ) ) );

    Map<String, Object> properties = context.getRootApplication().getProperties();

    assertEquals( false, properties.get( ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE ) );
  }

  @Test
  public void testRegsters_FEATURE_AUTO_DISCOVERY_DISABLE() {
    Configuration configuration = createConfiguration( "/test", false, 23L );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    JerseyContext context = new JerseyContext( httpService,
                                               configuration,
                                               servletConfigurationService,
                                               new ServiceContainer( mock( BundleContext.class ) ) );

    Map<String, Object> properties = context.getRootApplication().getProperties();

    assertEquals( true, properties.get( ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE ) );
  }

  @Test
  public void testAddResourceWithServletConfigurationServicePresent() throws Exception {
    Configuration configuration = createConfiguration( "/test", false, 23L );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    JerseyContext withConfiguration = spy( new JerseyContext( httpService,
                                                              configuration,
                                                              servletConfigurationService,
                                                              new ServiceContainer( mock( BundleContext.class ) ),
                                                              rootApplication,
                                                              servletContainerBridge ) );
    Object resource = new Object();
    withConfiguration.addResource( resource );
    verify( servletConfigurationService, times( 1 ) ).getHttpContext( any( HttpService.class ),
                                                                      anyString() );
    verify( servletConfigurationService, times( 1 ) ).getInitParams( any( HttpService.class ),
                                                                     anyString() );
  }

  @Test
  public void testAddsApplicationConfigurationsOnStart() {
    BundleContext context = mock( BundleContext.class );
    ServiceContainer container = new ServiceContainer( context );
    ServiceReference reference = mock( ServiceReference.class );
    ApplicationConfiguration appConfig = mock( ApplicationConfiguration.class );
    Map<String, Object> map = new HashMap<>();
    map.put( "foo", "bar" );
    when( appConfig.getProperties() ).thenReturn( map );
    when( context.getService( reference ) ).thenReturn( appConfig );
    container.add( reference );
    Configuration configuration = createConfiguration( "/test", false, 23L );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );

    JerseyContext jerseyContext = new JerseyContext( httpService,
                                                     configuration,
                                                     servletConfigurationService,
                                                     container );

    Map<String, Object> properties = jerseyContext.getRootApplication().getProperties();
    assertEquals( properties.get( "foo" ), "bar" );
  }

  @Test
  public void testUpdateConfigurationBeforeServletRegistered() {
    Configuration configuration = createConfiguration("/test2", true, 15000L);

    jerseyContext.updateConfiguration( configuration );

    verify(jerseyContext).updateConfiguration( configuration );
    verify(jerseyContext, never()).safeUnregister( "/test" );
    verify(jerseyContext, never()).registerServletWhenNotAlreadyRegistered();
  }

  @Test
  public void testUpdateConfigurationAfterServletRegistered() {
    Object resource = new Object();
    jerseyContext.addResource( resource );
    Configuration configuration = createConfiguration( "/test2", true, 15000L );

    jerseyContext.updateConfiguration( configuration );

    verify( jerseyContext ).updateConfiguration( configuration );
    verify( jerseyContext ).safeUnregister( "/test" );
    verify( jerseyContext, times( 2 ) ).registerServletWhenNotAlreadyRegistered();
  }

  @Test
  public void testUpdateAppConfiguration() {
    BundleContext context = mock( BundleContext.class );
    ServiceContainer container = new ServiceContainer( context );
    ServiceReference reference = mock( ServiceReference.class );
    ApplicationConfiguration appConfig = mock( ApplicationConfiguration.class );
    Map<String, Object> map = new HashMap<>();
    map.put( "foo", "bar" );
    when( appConfig.getProperties() ).thenReturn( map );
    when( context.getService( reference ) ).thenReturn( appConfig );
    container.add( reference );

    jerseyContext.updateAppConfiguration( container );

    verify( rootApplication ).addProperties( map );
  }

  @Test
  public void testUpdateServletConfigurationBeforeServletRegistered() {
    ServletConfiguration configuration = mock ( ServletConfiguration.class);

    jerseyContext.updateServletConfiguration( configuration );

    verify( jerseyContext ).updateServletConfiguration( configuration );
    verify( jerseyContext, never() ).safeUnregister( "/test" );
    verify( jerseyContext, never() ).registerServletWhenNotAlreadyRegistered();
  }

  @Test
  public void testUpdateServletConfigurationAfterServletRegistered() {
    Object resource = new Object();
    jerseyContext.addResource( resource );
    ServletConfiguration configuration = mock ( ServletConfiguration.class);

    jerseyContext.updateServletConfiguration( configuration );

    verify( jerseyContext ).updateServletConfiguration( configuration );
    verify( jerseyContext ).safeUnregister( "/test" );
    verify( jerseyContext, times( 2 ) ).registerServletWhenNotAlreadyRegistered();
  }
  
  @Test
  public void testRemoveResourceServletBridgeStatus()
    throws ServletException, NamespaceException, IOException, InterruptedException
  {
    Object monitor = new Object();
    JerseyContext jerseyContext = getJerseyContextWithSpyServletContainerBridgeRunMonitor( 1500L, monitor );
    ServletContainerBridge servletContainerBridge = jerseyContext.getServletContainerBridge();
    
    // Add two resources, but not the third
    Object resource1 = new Object();
    Object resource2 = new Object();
    Object resource3 = new Object();
    
    jerseyContext.addResource( resource1 );
    jerseyContext.addResource( resource2 );
    
    // Check if Jersey was loaded
    waitForMonitor( monitor, 1500L );
    verify( servletContainerBridge, timeout( Integer.MAX_VALUE ).times( 1 ) ).run();
    
    // Should not reload when removing a resource that is not present
    jerseyContext.removeResource( resource3 );
    verify( servletContainerBridge, times( 1 ) ).run();
    
    // Fire up a request - it should be serviced as jersey did not need to reload
    verifyServiceWasExecuted(servletContainerBridge, 1);
    
    // Jersy is marked as not ready and should reload
    jerseyContext.removeResource( resource2 );

    
    // Fire up a request again - should get a 503 as jersey will need to reload
    verifyServiceWasNotExecuted(servletContainerBridge, 1);
    waitForMonitor( monitor, 1500L );
    
    // Wait for the run method to be executed
    verify( servletContainerBridge, timeout( Integer.MAX_VALUE ).times( 2) ).run();
    
    // Fire up a request - it should be serviced after reload
    verifyServiceWasExecuted(servletContainerBridge, 2);
  }
  
  /**
   * Calls the service method of the given {@link ServletContainerBridge} and verifies that the
   * underlying {@link ServletContainer#service(ServletRequest, ServletResponse)} has been executed
   * the given numberOfTimes.
   * 
   * @param servletContainerBridge
   * @param numberOfTimes
   * @throws ServletException
   * @throws IOException
   */
  private void verifyServiceWasExecuted( ServletContainerBridge servletContainerBridge,
                                         int numberOfTimes ) throws ServletException, IOException
  {
    servletContainerBridge.service( mock( ServletRequest.class ), mock( ServletResponse.class ) );
    verify( servletContainerBridge.getServletContainer(), times( numberOfTimes ) )
      .service( any( ServletRequest.class ), any( ServletResponse.class ) );
  }
  
  /**
   * Calls the service method of the given {@link ServletContainerBridge} and verifies that the
   * {@link HttpServletResponse#sendError(int)} is executed with
   * HttpServletResponse.SC_SERVICE_UNAVAILABLE.
   * 
   * @param servletContainerBridge
   * @param numberOfTimes
   * @throws ServletException
   * @throws IOException
   */
  private void verifyServiceWasNotExecuted( ServletContainerBridge servletContainerBridge,
                                            int numberOfTimes )
                                              throws ServletException, IOException
  {
    HttpServletResponse response = mock( HttpServletResponse.class );
    servletContainerBridge.service( mock( ServletRequest.class ), response );
    verify( response, times( numberOfTimes ) )
      .sendError( eq( HttpServletResponse.SC_SERVICE_UNAVAILABLE ), any( String.class ) );
  }
  
  private JerseyContext getJerseyContextWithSpyServletContainerBridgeRunMonitor(Long publishDelay, final Object monitor) {
    Configuration configuration = createConfiguration( "/test", false, publishDelay );
    ServletConfiguration servletConfigurationService = mock( ServletConfiguration.class );
    
    RootApplication rootApplication = spy( new RootApplication() );
    ServletContainerBridge servletContainerBridge = spy( new ServletContainerBridge( rootApplication ) );
    
    ServletContainer spyContainer = mock( ServletContainer.class );
    when( servletContainerBridge.getServletContainer() ).thenReturn( spyContainer );
    
    // Mock destroy so that it will block until the given monitor is notified!
    doAnswer( new Answer<Object>() {

      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        invocation.callRealMethod();
        synchronized( monitor ) {
          monitor.notifyAll();
        }
        return null;
      }
    } ).when( servletContainerBridge ).run();
    
    
    return spy( new JerseyContext( httpService,
                                   configuration,
                                   servletConfigurationService,
                                   new ServiceContainer( mock( BundleContext.class ) ),
                                   rootApplication,
                                   servletContainerBridge ) );
  }
  
  private void waitForMonitor(Object monitor, Long timeout) throws InterruptedException {
    synchronized(monitor) {
      monitor.wait(timeout);
    }
  }

  private Configuration createConfiguration( String path, boolean wadlDisable, long publishDelay ) {
    Configuration configuration = mock( Configuration.class );
    when( configuration.getRoothPath() ).thenReturn( path );
    when( configuration.getPublishDelay() ).thenReturn( publishDelay );
    return configuration;
  }

}
