/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.junit.Test;


public class ContainerRequestFilterImplTest {
  
  @Test
  public void testHasProviderAnnotation() {
    boolean annotationPresent = ContainerRequestFilterImpl.class.isAnnotationPresent( Provider.class );
    
    assertTrue( annotationPresent );
  }
  
  @Test
  public void testHasAuthenticationPriotiry() {
    Priority priority = ContainerRequestFilterImpl.class.getAnnotation( Priority.class );
    
    int value = priority.value();
    
    assertEquals( Priorities.AUTHENTICATION, value );
  }
  
  @Test
  public void testSetsSecurityContext() throws IOException {
    SecurityAdmin securityAdmin = mock( SecurityAdmin.class );
    SecurityContext securityContext = mock( SecurityContext.class );
    when( securityAdmin.getSecurityContext( any( ContainerRequestContext.class ) ) ).thenReturn( securityContext );
    ContainerRequestFilterImpl filter = new ContainerRequestFilterImpl( securityAdmin );
    ContainerRequestContext context = mock( ContainerRequestContext.class );
    
    filter.filter( context );
    
    verify( context ).setSecurityContext( securityContext );
  }
  
  @Test
  public void testSetsSecurityContextOnlyIfAdminCanCreateOne() throws IOException {
    SecurityAdmin securityAdmin = mock( SecurityAdmin.class );
    when( securityAdmin.getSecurityContext( any( ContainerRequestContext.class ) ) ).thenReturn( null );
    ContainerRequestFilterImpl filter = new ContainerRequestFilterImpl( securityAdmin );
    ContainerRequestContext context = mock( ContainerRequestContext.class );
    
    filter.filter( context );
    
    verify( context, never() ).setSecurityContext( any( SecurityContext.class ) );
  }
  
  @Test
  public void testSetsSecurityContextOnlyWithAdmin() throws IOException {
    ContainerRequestFilterImpl filter = new ContainerRequestFilterImpl( null );
    ContainerRequestContext context = mock( ContainerRequestContext.class );
    
    filter.filter( context );
    
    verify( context, never() ).setSecurityContext( any( SecurityContext.class ) );
  }
}
