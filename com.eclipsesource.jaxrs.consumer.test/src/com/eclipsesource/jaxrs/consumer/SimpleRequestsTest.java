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
import com.github.restdriver.clientdriver.RestClientDriver;


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
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveResponse( "get" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContent() );
  }
  
  @Test
  public void testSimplePost() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( POST ), giveResponse( "post" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "post", resource.postContent() );
  }
  
  @Test
  public void testSimplePostWithContent() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveResponse( "postWithBody" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "postWithBody", resource.postContent( "test" ) );
  }
  
  @Test
  public void testSimplePut() {
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( PUT )
                           .withBody( "test", MediaType.TEXT_PLAIN ), giveResponse( "put" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "put", resource.putContent( "test" ) );
  }
  
  @Test
  public void testSimpleDelete() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( DELETE ), giveResponse( "delete" ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "delete", resource.deleteContent() );
  }
  
  @Test
  public void testSimpleHead() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( HEAD ), RestClientDriver.giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.head();
  }
  
  @Test
  public void testSimpleOptions() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( OPTIONS ), RestClientDriver.giveEmptyResponse() );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    resource.options();
  }
  
}
