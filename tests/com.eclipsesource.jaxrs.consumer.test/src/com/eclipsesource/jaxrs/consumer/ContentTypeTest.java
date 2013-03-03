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
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.PUT;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class ContentTypeTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    String postContent( String content );
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    String putContent( String content );
  }
  
  @Test
  public void testContenttypeWithSimplePostWithContent() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.APPLICATION_JSON ), 
                           giveResponse( "postWithBody", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "postWithBody", resource.postContent( "test" ) );
  }
  
  @Test
  public void testContenttypeWithSimplePut() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.APPLICATION_JSON ), giveResponse( "put", TEXT_PLAIN ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "put", resource.putContent( "test" ) );
  }
}
