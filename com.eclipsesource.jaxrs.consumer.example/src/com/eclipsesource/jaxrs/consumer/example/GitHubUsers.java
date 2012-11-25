package com.eclipsesource.jaxrs.consumer.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path( "/users" )
public interface GitHubUsers {
  
  @GET
  @Path( "/{user-id}" )
  GitHubUser getUser( @PathParam( "user-id" ) String userId );
  
}
