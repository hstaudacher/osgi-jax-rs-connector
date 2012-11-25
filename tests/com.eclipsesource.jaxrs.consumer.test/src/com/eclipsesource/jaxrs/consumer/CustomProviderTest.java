package com.eclipsesource.jaxrs.consumer;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.POST;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.jaxrs.consumer.test.TestUtil.CustomProvider;
import com.eclipsesource.jaxrs.consumer.test.TestUtil.CustomType;
import com.github.restdriver.clientdriver.ClientDriverRule;


public class CustomProviderTest {
  
  @Rule
  public ClientDriverRule driver = new ClientDriverRule();
  
  private FakeResource resource;
  
  @Path( "/test" )
  private interface FakeResource {
    @GET
    CustomType getContent();
    @POST
    CustomType postContent( CustomType content );
    @POST
    String postContent( String content );
  }
  
  @Before
  public void setUp() {
    CustomProvider customProvider = new CustomProvider();
    resource = ConsumerFactory.createConsumer( driver.getBaseUrl(), FakeResource.class, customProvider );
  }
  
  @Test
  public void testSimpleGetWithCustomProvider() {
    driver.addExpectation( onRequestTo( "/test" ).withMethod( GET ), giveResponse( "get" ) );
    
    CustomType content = resource.getContent();
    assertEquals( "get", content.getValue() );
  }
  
  @Test
  public void testSimplePostWithCustomProvider() {
    CustomType customType = new CustomType( "foo" );
    driver.addExpectation( onRequestTo( "/test" )
                           .withMethod( POST )
                           .withBody( customType.getValue(), TEXT_PLAIN ), giveResponse( "bar" ) );
    
    CustomType content = resource.postContent( customType );
    assertEquals( "bar", content.getValue() );
  }
}
