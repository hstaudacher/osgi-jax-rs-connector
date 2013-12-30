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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;


public class SecurityAdminTest {
  
  private AuthorizationHandler authorizationHandler;
  private AuthenticationHandler authenticationHandler;

  @Before
  public void setUp() {
    authorizationHandler = mock( AuthorizationHandler.class );
    authenticationHandler = mock( AuthenticationHandler.class );
    Activator activator = mock( Activator.class );
    when( activator.getAuthenticationHandler() ).thenReturn( authenticationHandler );
    when( activator.getAuthorizationHandler() ).thenReturn( authorizationHandler );
    Activator.setInstance( activator );
  }
  
  @After
  public void tearDown() {
    Activator.setInstance( null );
  }
  
  @Test
  public void testReturnsNullContextWithoutHandlers() {
    Activator.setInstance( mock( Activator.class ) );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    
    SecurityContext context = securityAdmin.getSecurityContext( mock( ContainerRequestContext.class ) );
    
    assertNull( context );
  }
  
  @Test
  public void testReturnsNullContextWhenAuthenticationFails() {
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( null );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    
    SecurityContext context = securityAdmin.getSecurityContext( mock( ContainerRequestContext.class ) );
    
    assertNull( context );
  }
  
  @Test
  public void testReturnsContextWhenAuthenticationSucceeds() throws URISyntaxException {
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( mock( Principal.class ) );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "http://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    
    assertNotNull( context );
  }
  
  @Test
  public void testUsesUriSchemeForDetectingHttp() throws URISyntaxException {
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( mock( Principal.class ) );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "http://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    
    assertFalse( context.isSecure() );
  }
  
  @Test
  public void testUsesUriSchemeForDetectingHttps() throws URISyntaxException {
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( mock( Principal.class ) );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "https://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    
    assertTrue( context.isSecure() );
  }
  
  @Test
  public void testUsesHandlerPrincipal() throws URISyntaxException {
    Principal principal = mock( Principal.class );
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( principal );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "https://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    
    assertSame( principal, context.getUserPrincipal() );
  }
  
  @Test
  public void testUsesHandlerAuthenticationScheme() throws URISyntaxException {
    Principal principal = mock( Principal.class );
    when( authenticationHandler.getAuthenticationScheme() ).thenReturn( SecurityContext.BASIC_AUTH );
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( principal );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "https://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    
    assertEquals( SecurityContext.BASIC_AUTH, context.getAuthenticationScheme() );
  }
  
  @Test
  public void testPassesAuthorizationHandler() throws URISyntaxException {
    Principal principal = mock( Principal.class );
    when( authenticationHandler.authenticate( any( ContainerRequestContext.class ) ) ).thenReturn( principal );
    SecurityAdmin securityAdmin = new SecurityAdmin();
    ContainerRequestContext requestContext = mockContainerRequest( "https://foo.bar" );
    
    SecurityContext context = securityAdmin.getSecurityContext( requestContext );
    context.isUserInRole( "foo" );

    verify( authorizationHandler ).isUserInRole( principal, "foo" );
  }

  private ContainerRequestContext mockContainerRequest( String uri ) throws URISyntaxException {
    ContainerRequestContext requestContext = mock( ContainerRequestContext.class );
    UriInfo uriInfo = mock( UriInfo.class );
    URI requestUri = new URI( uri );
    when( uriInfo.getRequestUri() ).thenReturn( requestUri );
    when( requestContext.getUriInfo() ).thenReturn( uriInfo );
    return requestContext;
  }
  
}
