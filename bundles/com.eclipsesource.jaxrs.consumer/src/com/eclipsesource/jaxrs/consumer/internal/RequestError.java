package com.eclipsesource.jaxrs.consumer.internal;

import javax.ws.rs.core.Response;


public class RequestError {

  private final String requestUrl;
  private final Response response;
  private final String method;

  public RequestError( RequestConfigurer configurer, Response response, String method ) {
    this.response = response;
    this.method = method;
    this.requestUrl = configurer.getRequestUrl();
  }

  public String getMessage() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append( "Failed to send " + method + " request to: " + requestUrl );
    stringBuilder.append( "\n" );
    stringBuilder.append( "Received Status: " + response.getStatus() );
    stringBuilder.append( "\nReceived Body: " );
    if( response.hasEntity() ) {
      String body = response.readEntity( String.class );
      stringBuilder.append( body );
    }
    stringBuilder.append( "\n" );
    return stringBuilder.toString();
  }
}
