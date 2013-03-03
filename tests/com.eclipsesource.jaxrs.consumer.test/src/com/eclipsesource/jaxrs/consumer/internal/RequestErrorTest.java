package com.eclipsesource.jaxrs.consumer.internal;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Test;


public class RequestErrorTest {
  
  @Test
  public void testContainsUrl() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    when( configurer.getRequestUrl() ).thenReturn( "http://foo.bar" );
    Response response = mock( Response.class );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "http://foo.bar" ) );
  }
  
  @Test
  public void testContainsStatus() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.getStatus() ).thenReturn( 666 );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "666" ) );
  }
  
  @Test
  public void testContainsMethod() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.getStatus() ).thenReturn( 666 );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "GET" ) );
  }
  
  @Test
  public void testContainsBody() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.hasEntity() ).thenReturn( true );
    when( response.readEntity( String.class ) ).thenReturn( "body" );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "body" ) );
  }
}
