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
package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.DELETE;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.HEAD;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.OPTIONS;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.POST;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.PUT;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;
import com.github.restdriver.clientdriver.RestClientDriver;


public class HeaderTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    String getContent( @HeaderParam( "foo" ) String param );
    @POST
    String postContent( @HeaderParam( "foo" ) String param );
    @POST
    String postContent( @HeaderParam( "foo" ) String param, String content );
    @PUT
    String putContent( @HeaderParam( "foo" ) String param, String content );
    @DELETE
    String deleteContent( @HeaderParam( "foo" ) String param );
    @HEAD
    void head( @HeaderParam( "foo" ) String param );
    @OPTIONS
    void options( @HeaderParam( "foo" ) String param );
  }
  
  @Test
  public void testSimpleGet() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( GET )
                           .withHeader( "foo", "bar" ), giveResponse( "get", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent( "bar" ) );
  }
  
  @Test
  public void testSimplePost() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withHeader( "foo", "bar" ), giveResponse( "post", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.postContent( "bar" ) );
  }
  
  @Test
  public void testSimplePostWithContent() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN )
                           .withHeader( "foo", "bar" ), giveResponse( "postWithBody", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "postWithBody", resource.postContent( "bar", "test" ) );
  }
  
  @Test
  public void testSimplePut() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN )
                           .withHeader( "foo", "bar" ), giveResponse( "put", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "put", resource.putContent( "bar", "test" ) );
  }
  
  @Test
  public void testSimpleDelete() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( DELETE )
                           .withHeader( "foo", "bar" ), giveResponse( "delete", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "delete", resource.deleteContent( "bar" ) );
  }
  
  @Test
  public void testSimpleHead() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( HEAD )
                           .withHeader( "foo", "bar" ), RestClientDriver.giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.head( "bar" );
  }
  
  @Test
  public void testSimpleOptions() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( OPTIONS )
                           .withHeader( "foo", "bar" ), RestClientDriver.giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.options( "bar" );
  }
}
