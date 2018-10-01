/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.WebComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class ServletContainerBridge_Test {

  RootApplication application;
  private ServletContainerBridge bridge;
  private ServletConfig config;
  private ServletContext context;

  @Before
  public void setUp() {
    application = mock( RootApplication.class );
    bridge = new ServletContainerBridge( application );
    config = mock(ServletConfig.class);
    context = mock(ServletContext.class);
    when(config.getServletContext()).thenReturn( context );
  }

  @Test
  public void testCreateServletContainer() {
    ServletContainer container = bridge.getServletContainer();

    assertNotNull( container );
  }

  @Test
  public void testCreateServletContainerOnce() {
    ServletContainer container = bridge.getServletContainer();
    ServletContainer container2 = bridge.getServletContainer();

    assertSame( container, container2 );
  }

  @Test
  public void testDestroyCreatesNewContainer() throws ServletException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    ServletContainer container1 = actualBridge.getServletContainer();
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( true );

    actualBridge.init(config);
    actualBridge.run();
    verify( container ).init( config );
    actualBridge.destroy();
    Mockito.reset( actualBridge );

    ServletContainer container2 = actualBridge.getServletContainer();
    assertNotSame( container1, container2 );
  }

  @Test
  public void testCallsDestroyDoesNotFail() {
    bridge.destroy();
  }

  @Test
  public void testRunInitOnDirty() throws ServletException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( true );

    actualBridge.init( config );
    actualBridge.run();

    verify( container ).init( config );
  }

  @Test
  public void testRunReloadsOnlyOnDirty() throws ServletException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( false );

    actualBridge.run();

    verify( container, never() ).init(any(FilterConfig.class));
    verify( container, never() ).reload( any( ResourceConfig.class ) );
  }

  @Test
  public void testRunSetsDirtyToFalseAfterRun() throws ServletException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = new ServletContainer();
    ServletContainer spyContainer = spy( container );
    doAnswer( new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        application.setDirty( false );
        return null;
      }
    } ).when( spyContainer ).init( config );
    when( actualBridge.getServletContainer() ).thenReturn( spyContainer );
    when( application.isDirty() ).thenReturn( true );

    actualBridge.init( config  );
    actualBridge.run();

    verify( application ).setDirty( false );
  }

  @Test
  public void testRunReloadAfterInit() throws ServletException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( true );
    when( container.getWebComponent() ).thenReturn( mock(WebComponent.class));

    actualBridge.run();

    verify( container, never() ).init(any(FilterConfig.class));
    verify( container).reload( any( ResourceConfig.class ) );
  }

  @Test
  public void testServiceWhenNotReady() throws ServletException, IOException {
    ServletContainerBridge actualBridge = spy( bridge );
    HttpServletResponse response = mock( HttpServletResponse.class );
    assertFalse( actualBridge.isJerseyReady() );

    actualBridge.service( mock(ServletRequest.class), response );

    verify( response ).sendError( eq( HttpServletResponse.SC_SERVICE_UNAVAILABLE ), any( String.class ) );
  }

  @Test
  public void testServiceWhenReady() throws ServletException, IOException {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( actualBridge.isJerseyReady() ).thenReturn( true );

    ServletRequest request = mock(ServletRequest.class);
    ServletResponse response = mock(ServletResponse.class);
    actualBridge.service( request, response );

    verify(container).service(request, response);
  }
  
  @Test
  public void testServiceWhenNotReadyAndDestroyIsRunningSlow()
    throws ServletException, IOException, InterruptedException
  {
    Object monitor = new Object();
    ServletContainerBridge actualBridge = getDestroyMockedServletBridge( monitor );
    
    // Initialize Jersey
    actualBridge.run();
    assertTrue( actualBridge.isJerseyReady() );
    
    // Async destroy
    runDestroyAsync( actualBridge );
    
    // Make sure destroy thread has started!
    waitForMonitor( monitor, 1000L );
    
    // Try to fire up a request - should get a 503 while destroying!
    verifyServiceWasNotExecuted(actualBridge);
    
    // finish up destroying
    notifyAllMonitor( monitor );
    
    // Fire up a request again should get a 503 after destroying!
    verifyServiceWasNotExecuted(actualBridge);
  }

  /**
   * @param monitor
   * @return A {@link ServletContainerBridge} with a mocked {@link ServletContainer#destroy()}
   *         method in such away that it blocks until the given monitor is notified
   */
  private ServletContainerBridge getDestroyMockedServletBridge( final Object monitor ) {
    final ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer spyContainer = mock( ServletContainer.class );
    when( application.isDirty() ).thenReturn( true );
    when( actualBridge.getServletContainer() ).thenReturn( spyContainer );
    // Mock destroy so that it will block until the given monitor is notified!
    doAnswer( new Answer<Object>() {

      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        synchronized( monitor ) {
          monitor.notifyAll();
          monitor.wait();
        }
        return null;
      }
    } ).when( spyContainer ).destroy();
    return actualBridge;
  }

  private void runDestroyAsync( final ServletContainerBridge bridge ) {
    // Start destroying the bridge asynchronously
    new Thread( new Runnable() {

      @Override
      public void run() {
        bridge.destroy();
      }
    } ).start();
  }

  private void waitForMonitor( Object monitor, Long timeout ) throws InterruptedException {
    synchronized( monitor ) {
      monitor.wait( timeout );
    }
  }

  private void notifyAllMonitor( Object monitor ) {
    synchronized( monitor ) {
      monitor.notifyAll();
    }
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
  private void verifyServiceWasNotExecuted( ServletContainerBridge servletContainerBridge)
                                              throws ServletException, IOException
  {
    HttpServletResponse response = mock( HttpServletResponse.class );
    servletContainerBridge.service( mock( ServletRequest.class ), response );
    verify( response, times( 1 ) )
      .sendError( eq( HttpServletResponse.SC_SERVICE_UNAVAILABLE ), any( String.class ) );
  }
  
}
