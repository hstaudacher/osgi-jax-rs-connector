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
import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class SimpleRequestsTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    String getContent();
    @POST
    String postContent();
    @POST
    String postContent( String content );
    @PUT
    String putContent( String content );
    @DELETE
    String deleteContent();
    @HEAD
    void head();
    @OPTIONS
    void options();
  }
  
  @Test
  public void testSimpleGet() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), 
                           giveResponse( "get", "text/plain" ).withStatus( 200 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent() );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleGetWith500() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.getContent();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleGetWith404() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.getContent();
  }
  
  @Test
  public void testSimplePost() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( POST ), 
                           giveResponse( "post", "text/plain" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.postContent() );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePostWith500() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( POST ), 
                           giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.postContent();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePostWith404() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( POST ), 
                           giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.postContent();
  }
  
  @Test
  public void testSimplePostWithContent() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveResponse( "postWithBody", "text/plain" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "postWithBody", resource.postContent( "test" ) );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePostWithContentWith500() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.postContent( "test" );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePostWithContentWith404() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.postContent( "test" );
  }
  
  @Test
  public void testSimplePut() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveResponse( "put", "text/plain" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "put", resource.putContent( "test" ) );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePutWith500() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.putContent( "test" );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimplePutWith404() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.putContent( "test" );
  }
  
  @Test
  public void testSimpleDelete() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( DELETE ), giveResponse( "delete", "text/plain" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "delete", resource.deleteContent() );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleDeleteWith500() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( DELETE ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.deleteContent();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleDeleteWith404() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( DELETE ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.deleteContent();
  }
  
  @Test
  public void testSimpleHead() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( HEAD ), giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.head();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleHeadWith500() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( HEAD ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.head();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleHeadWith404() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( HEAD ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.head();
  }
  
  @Test
  public void testSimpleOptions() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( OPTIONS ), giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.options();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleOptionsWith500() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( OPTIONS ), giveEmptyResponse().withStatus( 500 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.options();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSimpleOptionsWith404() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( OPTIONS ), giveEmptyResponse().withStatus( 404 ) );
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.options();
  }
  
}
