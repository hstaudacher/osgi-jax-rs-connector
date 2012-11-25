package com.eclipsesource.jaxrs.consumer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

public class TestUtil {

  public static <T> T createResource( Class<T> type, String baseUrl ) {
    return ConsumerFactory.createConsumer( baseUrl, type );
  }
  
  public static class CustomType {

    private final String value;

    public CustomType( String value ) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
  
  @Provider
  @Produces( MediaType.TEXT_PLAIN )
  public static class CustomProvider
    implements MessageBodyWriter<CustomType>, MessageBodyReader<CustomType>
  {

    @Override
    public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
      return CustomType.class.isAssignableFrom( type );
    }

    @Override
    public long getSize( CustomType t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
      return -1;
    }

    @Override
    public void writeTo( CustomType t,
                         Class<?> type,
                         Type genericType,
                         Annotation[] annotations,
                         MediaType mediaType,
                         MultivaluedMap<String, Object> httpHeaders,
                         OutputStream entityStream ) throws IOException, WebApplicationException
    {
      PrintWriter writer = new PrintWriter( entityStream );
      writer.write( t.getValue() );
      writer.flush();
    }

    @Override
    public boolean isReadable( Class<?> type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType )
    {
      return CustomType.class.isAssignableFrom( type );
    }

    @Override
    public CustomType readFrom( Class<CustomType> type,
                                Type genericType,
                                Annotation[] annotations,
                                MediaType mediaType,
                                MultivaluedMap<String, String> httpHeaders,
                                InputStream entityStream )
      throws IOException, WebApplicationException
    {
      return new CustomType( convertStreamToString( entityStream ) );
    }

    public String convertStreamToString( InputStream is ) throws IOException {
      Writer writer = new StringWriter();
      char[] buffer = new char[ 1024 ];
      try {
        Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
        int n;
        while( ( n = reader.read( buffer ) ) != -1 ) {
          writer.write( buffer, 0, n );
        }
      } finally {
        is.close();
      }
      return writer.toString();
    }
  }

  private TestUtil() {
    // prevent instantiation
  }
}
