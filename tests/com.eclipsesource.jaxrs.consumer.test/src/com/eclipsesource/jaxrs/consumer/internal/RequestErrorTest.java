package com.eclipsesource.jaxrs.consumer.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;


public class RequestErrorTest {
  
  @Test
  public void testMessageContainsUrl() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    when( configurer.getRequestUrl() ).thenReturn( "http://foo.bar" );
    Response response = mock( Response.class );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "http://foo.bar" ) );
  }
  
  @Test
  public void testMessageContainsStatus() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.getStatus() ).thenReturn( 666 );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "666" ) );
  }
  
  @Test
  public void testMessageContainsMethod() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.getStatus() ).thenReturn( 666 );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "GET" ) );
  }
  
  @Test
  public void testMessageContainsBody() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.hasEntity() ).thenReturn( true );
    when( response.readEntity( String.class ) ).thenReturn( "body" );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String message = requestError.getMessage();
    
    assertTrue( message.contains( "body" ) );
  }
  
  @Test
  public void testSavesEntityInternal() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.hasEntity() ).thenReturn( true );
    when( response.readEntity( String.class ) ).thenReturn( "body" );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    requestError.getMessage();
    requestError.getEntity();
    
    verify( response, times( 1 ) ).hasEntity();
    verify( response, times( 1 ) ).readEntity( String.class );
  }
  
  @Test
  public void testHasBody() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.hasEntity() ).thenReturn( true );
    when( response.readEntity( String.class ) ).thenReturn( "body" );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String entity = requestError.getEntity();
    
    assertEquals( "body", entity );
  }
  
  @Test
  public void testHasStatus() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    when( response.getStatus() ).thenReturn( 233 );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    int status = requestError.getStatus();
    
    assertEquals( 233, status );
  }
  
  @Test
  public void testHasMethod() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    Response response = mock( Response.class );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    String method = requestError.getMethod();
    
    assertEquals( "GET", method );
  }
  
  @Test
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void testHasUrl() {
    RequestConfigurer configurer = mock( RequestConfigurer.class );
    MultivaluedMap headers = mock( MultivaluedMap.class );
    Response response = mock( Response.class );
    when( response.getHeaders() ).thenReturn( headers );
    RequestError requestError = new RequestError( configurer, response, "GET" );
    
    MultivaluedMap<String, Object> actualHeaders = requestError.getHeaders();
    
    assertSame( headers, actualHeaders );
  }
}
