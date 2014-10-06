package com.eclipsesource.jaxrs.provider.gson;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;


@Provider
@Produces( APPLICATION_JSON )
@Consumes( APPLICATION_JSON )
public class GsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

  private Gson gson;

  public GsonProvider() {
    gson = new Gson();
  }
  
  public Gson getGson() {
    return gson;
  }
  
  public void setGson( Gson gson ) {
    validateGson( gson );
    this.gson = gson;
  }

  private void validateGson( Gson gson ) {
    if( gson == null ) {
      throw new IllegalArgumentException( "gson must not be null" );
    }
  }

  @Override
  public long getSize( T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType  ) {
    return -1;
  }

  @Override
  public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return true;
  }

  @Override
  public void writeTo( T object,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream entityStream ) throws IOException, WebApplicationException
  {
    final String json = gson.toJson( object );
    entityStream.write( json.getBytes( "UTF-8" ) );
    entityStream.flush();
  }

  @Override
  public boolean isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return true;
  }

  @Override
  public T readFrom( Class<T> type,
                     Type gnericType,
                     Annotation[] annotations,
                     MediaType mediaType,
                     MultivaluedMap<String, String> httpHeaders,
                     InputStream entityStream ) throws IOException, WebApplicationException
  {
    try( InputStreamReader reader = new InputStreamReader( entityStream, "UTF-8" ) ) {
      return gson.fromJson( reader, type );
    }
  }
}