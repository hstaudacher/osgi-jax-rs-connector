package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.POST;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class PathParamTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test/{foo}" )
  private interface FakeResource {
    @GET
    String getContent( @PathParam( "foo" ) String foo );
    @POST
    String getContent( @PathParam( "foo" ) String foo, String content );
  }
  
  @Test
  public void testGetWithSubPath() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( GET ), giveResponse( "get" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent( "foo" ) );
  }
  
  @Test
  public void testPostWithSubPath() {
    driver.addExpectation( onRequestTo( "/test/foo" )
                           .withMethod( POST )
                           .withBody( "content", TEXT_PLAIN ), giveResponse( "post" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.getContent( "foo", "content" ) );
  }
  
}
