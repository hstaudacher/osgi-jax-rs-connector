package com.eclipsesource.jaxrs.connector.example.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

@Provider
public class MustacheTemplateProcessor implements TemplateProcessor<Mustache> {

  private final MustacheFactory mustacheFactory;

  public MustacheTemplateProcessor() {
    mustacheFactory = new DefaultMustacheFactory();
  }

  @Override
  public Mustache resolve( String name, MediaType mediaType ) {
    InputStream stream = MustacheTemplateProcessor.class.getResourceAsStream( name );
    return mustacheFactory.compile( new InputStreamReader( stream ), name );
  }

  @Override
  public void writeTo( Mustache mustache,
                       Viewable viewable,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream out ) throws IOException
  {
    Charset encoding = createContentType( mediaType, httpHeaders );
    mustache.execute( new OutputStreamWriter( out, encoding ), viewable.getModel() ).flush();
  }

  private Charset createContentType( MediaType mediaType, MultivaluedMap<String, Object> httpHeaders ) {
    Charset encoding;
    final String charset = mediaType.getParameters().get( MediaType.CHARSET_PARAMETER );
    MediaType finalMediaType;
    if( charset == null ) {
      encoding = Charset.forName( "utf-8" );
      Map<String, String> params = new HashMap<String, String>( mediaType.getParameters() );
      params.put( MediaType.CHARSET_PARAMETER, encoding.name() );
      finalMediaType = new MediaType( mediaType.getType(), mediaType.getSubtype(), params );
    } else {
      encoding = Charset.forName( charset );
      finalMediaType = mediaType;
    }
    List<Object> typeList = new ArrayList<Object>( 1 );
    typeList.add( finalMediaType.toString() );
    httpHeaders.put( HttpHeaders.CONTENT_TYPE, typeList );
    return encoding;
  }
}
