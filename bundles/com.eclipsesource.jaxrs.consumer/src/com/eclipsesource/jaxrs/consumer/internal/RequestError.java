package com.eclipsesource.jaxrs.consumer.internal;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


public class RequestError {

  private final String requestUrl;
  private final Response response;
  private final String method;
  private final String entity;

  public RequestError( RequestConfigurer configurer, Response response, String method ) {
    this.response = response;
    this.method = method;
    this.requestUrl = configurer.getRequestUrl();
    this.entity = getInternalEntity();
  }
  
  public String getInternalEntity() {
    if( response.hasEntity() ) {
      return response.readEntity( String.class );
    }
    return null;
  }

  public int getStatus() {
    return response.getStatus();
  }

  public String getMessage() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append( "Failed to send " + method + " request to: " + requestUrl );
    stringBuilder.append( "\n" );
    stringBuilder.append( "Received Status: " + response.getStatus() );
    if( entity != null ) {
      stringBuilder.append( "\nReceived Body: " );
      stringBuilder.append( entity );
    }
    stringBuilder.append( "\n" );
    return stringBuilder.toString();
  }
  
  public String getEntity() {
    return entity;
  }

  public String getMethod() {
    return method;
  }
  
  public String getRequestUrl() {
    return requestUrl;
  }
  
  public MultivaluedMap<String, Object> getHeaders() {
    return response.getHeaders();
  }
  
}
