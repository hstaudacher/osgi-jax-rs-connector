package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
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

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class PathTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    @Path( "foo" )
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
  public void testSimpleGetWithNonSlashPath() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( GET ), 
                           giveResponse( "get", "text/plain" ).withStatus( 200 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent() );
  }
}
