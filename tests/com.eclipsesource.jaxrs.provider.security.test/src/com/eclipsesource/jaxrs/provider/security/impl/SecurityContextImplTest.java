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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.junit.Test;

import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;


public class SecurityContextImplTest {
  
  @Test
  public void testIsInRoleIsUsesAuthorizationHandler_True() {
    AuthorizationHandler handler = mock( AuthorizationHandler.class );
    when( handler.isUserInRole( any( Principal.class ), anyString() ) ).thenReturn( true );
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), false, handler );
    
    boolean isUserInRole = context.isUserInRole( "foo" );
    
    assertTrue( isUserInRole );
  }
  
  @Test
  public void testIsInRoleIsUsesAuthorizationHandler_False() {
    AuthorizationHandler handler = mock( AuthorizationHandler.class );
    when( handler.isUserInRole( any( Principal.class ), anyString() ) ).thenReturn( false );
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), false, handler );
    
    boolean isUserInRole = context.isUserInRole( "foo" );
    
    assertFalse( isUserInRole );
  }
  
  @Test
  public void testIsInRoleIsTrueWithoutAuthorizationHandler() {
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), false, null );
    
    boolean isUserInRole = context.isUserInRole( "foo" );
    
    assertFalse( isUserInRole );
  }
  
  @Test
  public void testUsesScheme() {
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), false, null );
    
    String scheme = context.getAuthenticationScheme();
    
    assertEquals( SecurityContext.BASIC_AUTH, scheme );
  }
  
  @Test
  public void testUsesSecure_True() {
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), true, null );
    
    boolean secure = context.isSecure();
    
    assertTrue( secure );
  }
  
  @Test
  public void testUsesSecure_False() {
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, mock( Principal.class ), false, null );
    
    boolean secure = context.isSecure();
    
    assertFalse( secure );
  }
  
  @Test
  public void testUsesPrincipal() {
    Principal principal = mock( Principal.class );
    SecurityContextImpl context = new SecurityContextImpl( SecurityContext.BASIC_AUTH, principal, false, null );
    
    Principal userPrincipal = context.getUserPrincipal();
    
    assertSame( principal, userPrincipal );
  }
}
