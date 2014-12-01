/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.message.internal.MediaTypes;


public class RequestConfigurer {
  
  private final String baseUrl;
  private final Client client;
  private final Method method;
  private final Object[] parameter;

  public RequestConfigurer( Client client, String baseUrl, Method method, Object[] parameter ) {
    this.client = client;
    this.baseUrl = baseUrl;
    this.method = method;
    this.parameter = parameter;
  }
  
  public Builder configure() {
    WebTarget target = computeTarget();
    target = addQueryParameters( target );
    target = addMatrixParameters( target );
    Builder request = target.request();
    request = addHeaders( request );
    return request;
  }
  
  public String getRequestUrl() {
    WebTarget target = computeTarget();
    return target.getUri().toString();
  }
  
  private WebTarget computeTarget() {
    String serviceUrl = baseUrl;
    if( method.isAnnotationPresent( Path.class ) ) {
      String path = method.getAnnotation( Path.class ).value();
      serviceUrl = computeUrl( serviceUrl, path );
    }
    return client.target( replacePathParams( serviceUrl, method, parameter ) );
  }

  private String computeUrl( String serviceUrl, String path ) {
    if( serviceUrl.endsWith( "/" ) && path.startsWith( "/" ) ) {
      return serviceUrl + path.substring( 1, path.length() );
    } else if( !serviceUrl.endsWith( "/" ) && !path.startsWith( "/" ) ) {
      return serviceUrl + "/" + path;
    }
    return serviceUrl + path;
  }

  private String replacePathParams( String serviceUrl, Method method, Object[] parameter ) {
    String result = serviceUrl;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      String paramName = extractPathParam( annotations );
      if( paramName != null ) {
        result = result.replace( getPathSegment( result, paramName ), parameter[ i ].toString() );
      }
    }
    validatePath( result );
    return result;
  }

  private String getPathSegment( String fullPath, String pathParameter ) {
    Pattern pattern = Pattern.compile( ".*?(\\{" + pathParameter + ":*?.*?\\}).*?" );
    Matcher matcher = pattern.matcher( fullPath );
    if( matcher.matches() ) {
      return matcher.group( 1 );
    }
    return pathParameter;
  }

  private String extractPathParam( Annotation[] annotations ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == PathParam.class ) {
        return ( ( PathParam )annotation ).value();
      }
    }
    return null;
  }

  private void validatePath( String path ) {
    Pattern pattern = Pattern.compile( ".*?\\{(.+)\\}.*?" );
    Matcher matcher = pattern.matcher( path );
    if( matcher.matches() ) {
      String message = "Path " + path + " has undefined path parameters: ";
      for( int i = 1; i <= matcher.groupCount(); i++ ) {
        message += matcher.group( i ) + " ";
      }
      throw new IllegalStateException( message );
    }
  }
  
  private WebTarget addQueryParameters( WebTarget target ) {
    WebTarget result = target;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      String paramName = extractQueryParam( annotations );
      if( paramName != null ) {
        result = result.queryParam( paramName, parameter[ i ] );
      }
    }
    return result;
  }

  private String extractQueryParam( Annotation[] annotations ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == QueryParam.class ) {
        return ( ( QueryParam )annotation ).value();
      }
    }
    return null;
  }
  
  private WebTarget addMatrixParameters( WebTarget target ) {
    WebTarget result = target;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      String paramName = extractMatrixParam( annotations );
      if( paramName != null ) {
        result = result.matrixParam( paramName, parameter[ i ] );
      }
    }
    return result;
  }

  private String extractMatrixParam( Annotation[] annotations ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == MatrixParam.class ) {
        return ( ( MatrixParam )annotation ).value();
      }
    }
    return null;
  }
  
  private Builder addHeaders( Builder request ) {
    Builder result = request;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      String paramName = extractHeaderParam( annotations );
      if( paramName != null ) {
        result = result.header( paramName, parameter[ i ] );
      }
    }
    result = addAcceptHeader( request );
    return result;
  }
  
  private Builder addAcceptHeader( Builder request ) {
    Builder result = request;
    MediaType accept = MediaType.WILDCARD_TYPE;
    if( method.isAnnotationPresent( Produces.class ) ) {
      accept = MediaTypes.createFrom( method.getAnnotation( Produces.class ) ).get( 0 );
      result = result.header( HttpHeaders.ACCEPT, accept );
    }
    return result;
  }

  private String extractHeaderParam( Annotation[] annotations ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == HeaderParam.class ) {
        return ( ( HeaderParam )annotation ).value();
      }
    }
    return null;
  }
}
