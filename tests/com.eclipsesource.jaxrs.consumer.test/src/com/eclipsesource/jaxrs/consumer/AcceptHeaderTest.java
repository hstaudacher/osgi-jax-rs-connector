package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.DELETE;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.POST;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.PUT;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class AcceptHeaderTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    @Produces( TEXT_PLAIN )
    String getContent();
    @POST
    @Produces( TEXT_PLAIN )
    String postContent();
    @POST
    @Produces( TEXT_PLAIN )
    String postContent( String content );
    @PUT
    @Produces( TEXT_PLAIN )
    String putContent( String content );
    @DELETE
    @Produces( TEXT_PLAIN )
    String deleteContent();
  }
  
  @Test
  public void testAcceptHeaderWithSimpleGet() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( GET )
                           .withHeader( ACCEPT, TEXT_PLAIN ), giveResponse( "get" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent() );
  }
  
  @Test
  public void testAcceptHeaderWithSimplePost() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withHeader( ACCEPT, TEXT_PLAIN ), giveResponse( "post" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.postContent() );
  }
  
  @Test
  public void testAcceptHeaderWithSimplePostWithContent() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN )
                           .withHeader( ACCEPT, TEXT_PLAIN ), giveResponse( "postWithBody" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "postWithBody", resource.postContent( "test" ) );
  }
  
  @Test
  public void testAcceptHeaderWithSimplePut() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN )
                           .withHeader( ACCEPT, TEXT_PLAIN ), giveResponse( "put" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "put", resource.putContent( "test" ) );
  }
  
  @Test
  public void testAcceptHeaderWithSimpleDelete() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( DELETE )
                           .withHeader( ACCEPT, TEXT_PLAIN ), giveResponse( "delete" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "delete", resource.deleteContent() );
  }
  
}
