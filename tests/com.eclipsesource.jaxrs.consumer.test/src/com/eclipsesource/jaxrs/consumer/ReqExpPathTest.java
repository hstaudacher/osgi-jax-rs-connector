package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class ReqExpPathTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    @Path( "{path:.*}" )
    String getContentWith( @PathParam( "path" ) String foo );
    @GET
    @Path( "{path}" )
    String getContentWithout( @PathParam( "path" ) String foo );
  }
  
  @Test
  public void testSimpleGetWithRegexp() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( GET ), 
                           giveResponse( "get", "text/plain" ).withStatus( 200 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContentWith( "foo" ) );
  }
  
  @Test
  public void testSimpleGetWithoutRegExp() {
    driver.addExpectation( onRequestTo( "/test/foo" ).withMethod( GET ), 
                           giveResponse( "get", "text/plain" ).withStatus( 200 ) );
    
    FakeResource resource = createResource( FakeResource.class, driver.getBaseUrl() );
    
    assertEquals( "get", resource.getContentWithout( "foo" ) );
  }
}
