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
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class AdvancedGetRequestsTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    @Path( "/foo" )
    String getContent();
    @GET
    String getContent( @QueryParam( "foo" ) String param );
    @GET
    @Path( "/{foo}/{foo2}" )
    String getContent( @PathParam( "foo" ) String foo, @PathParam( "foo2" ) String foo2 );
    @GET
    String getContent( @MatrixParam( "foo" ) String foo, 
                       @MatrixParam( "foo1" ) String foo1,
                       @MatrixParam( "foo2" ) String foo2  );
  }
  
  @Test
  public void testGetWithSubPath() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( GET ), giveResponse( "get", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent() );
  }
  
  @Test
  public void testGetWithQueryParam() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ).withParam( "foo", "bar" ), 
                           giveResponse( "get", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent( "bar" ) );
  }
  
  @Test
  public void testGetWithPathParam() {
    driver.addExpectation( onRequestTo( "/test/bar/bar2" ).withMethod( GET ), giveResponse( "get", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent( "bar", "bar2" ) );
  }
  
  @Test
  public void testGetWithMatrixParams() {
//    TODO: Extend ClientDriver to not strip of the matrix paramters. See HttpRealRequest contructor.
//    driver.addExpectation( onRequestTo( "/test;foo=bar;foo1=bar1;foo2=bar2" ).withMethod( GET ), giveResponse( "get" ) );
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveResponse( "get", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent( "bar", "bar1", "bar2" ) );
  }
  
  @Test( expected = RequestException.class )
  public void testThrowsRequestExceptionWith4xx() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveResponse( "get", TEXT_PLAIN ).withStatus( 404 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.getContent( "bar", "bar1", "bar2" );
  }
  
  @Test( expected = RequestException.class )
  public void testThrowsRequestExceptionWith5xx() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveResponse( "get", TEXT_PLAIN ).withStatus( 500 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.getContent( "bar", "bar1", "bar2" );
  }
  
}
