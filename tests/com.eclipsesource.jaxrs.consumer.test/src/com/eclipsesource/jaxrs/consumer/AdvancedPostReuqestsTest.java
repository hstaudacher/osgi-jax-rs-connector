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
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.POST;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.FormParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class AdvancedPostReuqestsTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @POST
    @Path( "/foo" )
    String getContent();
    @POST
    String getContent( @FormParam( "foo" ) String param );
    @POST
    @Path( "/{foo}/{foo2}" )
    String getContent( @PathParam( "foo" ) String foo, @PathParam( "foo2" ) String foo2 );
    @POST
    String getContent( @MatrixParam( "foo" ) String foo, 
                       @MatrixParam( "foo1" ) String foo1,
                       @MatrixParam( "foo2" ) String foo2  );
  }
  
  @Test
  public void testPostWithSubPath() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( POST ), giveResponse( "post", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.getContent() );
  }
  
  @Test
  public void testPostWithFormParam() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withParam( "foo", "bar" )
                           .withHeader( CONTENT_TYPE, APPLICATION_FORM_URLENCODED ), 
                           giveResponse( "post", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.getContent( "bar" ) );
  }
  
  @Test
  public void testPostWithPathParam() {
    driver.addExpectation( onRequestTo( "/test/bar/bar2" ).withMethod( POST ), giveResponse( "post", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.getContent( "bar", "bar2" ) );
  }
  
  @Test
  public void testPostWithMatrixParams() {
//    TODO: Extend ClientDriver to not strip of the matrix paramters. See HttpRealRequest contructor.
//    driver.addExpectation( onRequestTo( "/test;foo=bar;foo1=bar1;foo2=bar2" ).withMethod( GET ), giveResponse( "post" ) );
    driver.addExpectation( onRequestTo( "/test" ).withMethod( POST ), giveResponse( "post", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.getContent( "bar", "bar1", "bar2" ) );
  }
}
