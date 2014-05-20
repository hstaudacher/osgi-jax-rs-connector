package com.eclipsesource.jaxrs.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.eclipsesource.jaxrs.consumer.internal.RequestError;


public class RequestExceptionTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutError() {
    new RequestException( null );
  }
  
  @Test
  public void testHasStatus() {
    RequestError error = mock( RequestError.class );
    when( error.getStatus() ).thenReturn( 233 );
    RequestException exception = new RequestException( error );
    
    int status = exception.getStatus();
    
    assertEquals( 233, status );
  }
  
  @Test
  public void testHasMessage() {
    RequestError error = mock( RequestError.class );
    when( error.getMessage() ).thenReturn( "foo" );
    RequestException exception = new RequestException( error );
    
    String message = exception.getMessage();
    
    assertEquals( "foo", message );
  }
  
  @Test
  public void testHasEntity() {
    RequestError error = mock( RequestError.class );
    when( error.getEntity() ).thenReturn( "foo" );
    RequestException exception = new RequestException( error );
    
    String entity = exception.getEntity();
    
    assertEquals( "foo", entity );
  }
  
  @Test
  public void testHasMethod() {
    RequestError error = mock( RequestError.class );
    when( error.getMethod() ).thenReturn( "foo" );
    RequestException exception = new RequestException( error );
    
    String method = exception.getMethod();
    
    assertEquals( "foo", method );
  }
  
  @Test
  public void testHasUrl() {
    RequestError error = mock( RequestError.class );
    when( error.getRequestUrl() ).thenReturn( "foo" );
    RequestException exception = new RequestException( error );
    
    String url = exception.getRequestUrl();
    
    assertEquals( "foo", url );
  }
  
  @Test
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void testHasHeaders() {
    RequestError error = mock( RequestError.class );
    when( error.getRequestUrl() ).thenReturn( "foo" );
    MultivaluedMap headers = mock( MultivaluedMap.class );
    when( error.getHeaders() ).thenReturn( headers );
    RequestException exception = new RequestException( error );
    
    MultivaluedMap<String, Object> actualHeaders = exception.getHeaders();
    
    assertSame( headers, actualHeaders );
  }
}
