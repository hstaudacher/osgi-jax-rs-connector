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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class ServletContainerBridge_Test {
  
  RootApplication application;
  private ServletContainerBridge bridge;

  @Before
  public void setUp() {
    application = mock( RootApplication.class );
    bridge = new ServletContainerBridge( application );
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
  public void testResetCreatesNewContainer() {
    ServletContainer container = bridge.getServletContainer();
    bridge.reset();
    ServletContainer container2 = bridge.getServletContainer();
    
    assertNotSame( container, container2 );
  }
  
  @Test
  public void testCallsDestroyDoesNotFail() {
    bridge.destroy();
  }
  
  @Test
  public void testRunReloadsOnDirty() {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( true );
    
    actualBridge.run();
    
    verify( container ).reload( any( ResourceConfig.class ) );
  }
  
  @Test
  public void testRunReloadsOnlyOnDirty() {
    ServletContainerBridge actualBridge = spy( bridge );
    ServletContainer container = mock( ServletContainer.class );
    when( actualBridge.getServletContainer() ).thenReturn( container );
    when( application.isDirty() ).thenReturn( false );
    
    actualBridge.run();
    
    verify( container, never() ).reload( any( ResourceConfig.class ) );
  }
  
  @Test
  public void testRunSetsDirtyToFalseAfterRun() {
    ServletContainerBridge actualBridge = spy( bridge );
    
    ServletContainer container = new ServletContainer();
    ServletContainer spyContainer = spy( container );
    
    Mockito.doAnswer( new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        application.setDirty( false );
        return null;
      }
    } ).when( spyContainer ).reload( any( ResourceConfig.class ) );
    
    when( actualBridge.getServletContainer() ).thenReturn( spyContainer );
    when( application.isDirty() ).thenReturn( true );
    
    actualBridge.run();
    
    verify( application ).setDirty( false );
  }
}
