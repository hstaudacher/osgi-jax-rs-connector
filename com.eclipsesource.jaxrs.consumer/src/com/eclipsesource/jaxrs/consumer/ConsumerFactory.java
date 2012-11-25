package com.eclipsesource.jaxrs.consumer;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Path;

import com.eclipsesource.jaxrs.consumer.internal.ResourceInvocationHandler;


public class ConsumerFactory {

  public static <T> T createConsumer( String baseUrl, Class<T> type ) {
    return createConsumer( baseUrl, type, new Object[] {} );
  }
  
  @SuppressWarnings( "unchecked" )
  public static <T> T createConsumer( String baseUrl, Class<T> type, Object... customProvider ) {
    validateArguments( baseUrl, type );
    Path path = type.getAnnotation( Path.class );
    return ( T )Proxy.newProxyInstance( type.getClassLoader(), 
                                        new Class<?>[] { type }, 
                                        new ResourceInvocationHandler( baseUrl + path.value(), customProvider ) );
  }

  private static <T> void validateArguments( String baseUrl, Class<T> type ) {
    checkUrl( baseUrl );
    checkType( type );
    checkAnnotation( type );
    ensureTypeIsAnInterface( type );
  }
  
  private static void checkUrl( String url ) {
    try {
      new URL( url );
    } catch( MalformedURLException invalidUrlException ) {
      throw new IllegalArgumentException( url + " is not a valid url", invalidUrlException );
    }
  }

  private static void checkType( Class<?> type ) {
    if( type == null ) {
      throw new IllegalArgumentException( "type must not be null." );
    }
  }

  private static void checkAnnotation( Class<?> type ) {
    if( !type.isAnnotationPresent( Path.class ) ) {
      throw new IllegalArgumentException( type.getName() + " is not a Resource. No @Path Annotation." );
    }
  }
  
  private static void ensureTypeIsAnInterface( Class<?> type ) {
    if( !type.isInterface() ) {
      throw new IllegalArgumentException( type.getName() + " is not an interface. You do not " +
      		                              "want a dependency to cglib, do you?" );
    }
  }
  
  private ConsumerFactory() {
    // prevent instantiation
  }
}
