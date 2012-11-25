package com.eclipsesource.jaxrs.consumer.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.internal.MediaTypes;


public class ResourceInvocationHandler implements InvocationHandler {

  private final Client client;
  private final String baseUrl;

  public ResourceInvocationHandler( String serviceUrl, Object... customProviders ) {
    ClientConfig config = ClientHelper.createClientConfig();
    registerProviders( config, customProviders );
    this.client = ClientFactory.newClient( config );
    this.baseUrl = serviceUrl;
  }

  private void registerProviders( ClientConfig config, Object[] customProviders ) {
    if( customProviders != null ) {
      for( Object provider : customProviders ) {
        if( provider.getClass().isAnnotationPresent( Provider.class ) ) {
          config.register( provider );
        } else {
          throw new IllegalArgumentException( provider.getClass().getName() + " is not annotated with @Provider" );
        }
      }
    }
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] parameter ) throws Throwable {
    RequestConfigurer configurer = new RequestConfigurer( client, baseUrl );
    Builder request = configurer.configure( method, parameter );
    return sendRequest( method, parameter, request );
  }

  private Object sendRequest( Method method, Object[] parameter, Builder request ) {
    Object result = null;
    if( method.isAnnotationPresent( GET.class ) ) {
      checkHasNoFormParameter( method );
      result = request.get().readEntity(  method.getReturnType()  );
    } if( method.isAnnotationPresent( POST.class ) ) {
      result = request.post( getPostEntity( method, parameter ) ).readEntity( method.getReturnType() );
    } if( method.isAnnotationPresent( PUT.class ) ) {
      result = request.put( getEntity( method, parameter ) ).readEntity( method.getReturnType() );
    } if( method.isAnnotationPresent( DELETE.class ) ) {
      result = request.delete().readEntity( method.getReturnType() );
    } if( method.isAnnotationPresent( HEAD.class ) ) {
      result = request.head().readEntity( method.getReturnType() );
    } if( method.isAnnotationPresent( OPTIONS.class ) ) {
      result = request.options().readEntity( method.getReturnType() );
    }
    return result;
  }

  private void checkHasNoFormParameter( Method method ) {
    if( hasFormParameter( method ) ) {
      throw new IllegalStateException( "@GET methods can not have @FormParam parameters." );
    }
  }

  private Entity<?> getPostEntity( Method method, Object[] parameter ) {
    Entity<?> result;
    try {
      result = getEntity( method, parameter );
    } catch( IllegalStateException noEntityException ) {
      result = null;
    }
    return result;
  }

  private Entity<?> getEntity( Method method, Object[] parameter ) {
    Entity<?> result = null;
    if( hasFormParameter( method ) ) {
      Form form = computeForm( method, parameter );
      result = Entity.form( form );
    } else {
      result = determineBodyParameter( method, parameter );
    }
    return result;
  }

  private boolean hasFormParameter( Method method ) {
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      for( Annotation annotation : annotations ) {
        if( annotation.annotationType() == FormParam.class ) {
          return true;
        }
      }
    }
    return false;
  }

  private Form computeForm( Method method, Object[] parameter ) {
    Form result = new Form();
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      String paramName = extractFormParam( annotations );
      if( paramName != null ) {
        result.param( paramName, parameter[ i ].toString() );
      }
    }
    return result.asMap().isEmpty() ? null : result;
  }

  private String extractFormParam( Annotation[] annotations ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == FormParam.class ) {
        return ( ( FormParam )annotation ).value();
      }
    }
    return null;
  }

  private Entity<?> determineBodyParameter( Method method, Object[] parameter ) {
    Entity<?> result = null;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    checkParametersForAnnotation( method, parameterAnnotations );
    int firstNonAnnotatedParameter = getFirstNonAnnotatedParameter( parameterAnnotations );
    if( firstNonAnnotatedParameter != -1 ) {
      result =  Entity.entity( parameter[ firstNonAnnotatedParameter ], determineContentType( method ) );
    } else {
      throw new IllegalStateException( "Can not find entity for method " + method.getName() 
                                       + ". It has no non-annotated parameter" );
    }
    return result;
  }

  private int getFirstNonAnnotatedParameter( Annotation[][] parameterAnnotations ) {
    int firstNonAnnotatedParameter = -1;
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      if( annotations.length == 0 ) {
        firstNonAnnotatedParameter = i;
        break;
      }
    }
    return firstNonAnnotatedParameter;
  }

  private void checkParametersForAnnotation( Method method, Annotation[][] parameterAnnotations ) {
    if( parameterAnnotations.length == 0 ) {
      throw new IllegalStateException( "Can not find entity for method " 
                                       + method.getName() + ". It has no paramters." );
    }
  }

  // TODO: Think about a better determination of the content type
  private MediaType determineContentType( Method method ) {
    MediaType result = MediaType.TEXT_PLAIN_TYPE;
    if( method.isAnnotationPresent( Consumes.class ) ) {
      result = MediaTypes.createFrom( method.getAnnotation( Consumes.class ) ).get( 0 );
    }
    return result;
  }
}
