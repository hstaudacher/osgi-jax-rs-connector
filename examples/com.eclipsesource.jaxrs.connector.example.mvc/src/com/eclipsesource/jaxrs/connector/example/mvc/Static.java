package com.eclipsesource.jaxrs.connector.example.mvc;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path( "/static" )
public class Static {
  
  @GET
  @Path("{path:.*}")
  @Produces( MediaType.WILDCARD )
  public InputStream get( @PathParam( "path" ) String path ) throws IOException {
    return Static.class.getResourceAsStream( "/" + path );
  }
  
}
