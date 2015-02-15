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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class ResourcePublisher_Test {
  
  private ResourcePublisher resourcePublisher;
  private ServletContainerBridge servletContainerBridge;
  ScheduledFuture<?> future;

  @Before
  public void setUp() {
    servletContainerBridge = mock( ServletContainerBridge.class );
    future = mock( ScheduledFuture.class );
    resourcePublisher = new ResourcePublisher( createSameThreadExecutor(), servletContainerBridge, 100 );
  }
  
  @Test
  public void testScheduleExecutesBridge() {
    resourcePublisher.schedulePublishing();
    
    verify( servletContainerBridge ).run();
  }
  
  @Test
  public void testScheduleTwiceCancelsPreviousFuture() {
    resourcePublisher.schedulePublishing();
    resourcePublisher.schedulePublishing();
    
    verify( servletContainerBridge, times( 2 ) ).run();
    verify( future ).cancel( false );
  }
  
  @Test
  public void testCancelCancelsPreviousFuture() {
    resourcePublisher.schedulePublishing();
    
    resourcePublisher.cancelPublishing();
    
    verify( future ).cancel( true );
  }

  private ScheduledExecutorService createSameThreadExecutor() {
    ScheduledExecutorService executorService = mock( ScheduledExecutorService.class );
    doAnswer( new Answer<ScheduledFuture<?>>() {

      @Override
      public ScheduledFuture<?> answer( InvocationOnMock invocation ) throws Throwable {
        Runnable runnable = ( Runnable )invocation.getArguments()[ 0 ];
        runnable.run();
        return future;
      }
    } ).when( executorService ).schedule( any( Runnable.class ), anyLong(), eq( TimeUnit.MILLISECONDS ) );
    return executorService;
  }
}
