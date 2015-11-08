/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.example.swagger;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path( "/users" )
@Api( value = "/users" )
public class UserService {

  private final List<User> users;

  public UserService() {
    users = new ArrayList<>();
  }

  @GET
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( MediaType.APPLICATION_JSON )
  @ApiOperation( value = "Get all users", response = User.class, responseContainer = "List" )
  public List<User> getUsers() {
    return users;
  }

  @PUT
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( MediaType.APPLICATION_JSON )
  @ApiOperation( value = "Add user", response = User.class )
  public User addUser( User user ) {
    users.add( user );
    return user;
  }

  @POST
  @Path( "/{username}" )
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( MediaType.APPLICATION_JSON )
  @ApiOperation( value = "Update user", response = User.class )
  public User updateUser( @PathParam( "username" ) String name, User user ) {
    for( User storedUser : new ArrayList<>( users ) ) {
      if( storedUser.getName().equals( name ) ) {
        users.remove( storedUser );
      }
    }
    users.add( user );
    return user;
  }

  @GET
  @Path( "/{username}" )
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( MediaType.APPLICATION_JSON )
  @ApiOperation( value = "Get user", response = User.class )
  public User getUser( @PathParam( "username" ) String name ) {
    for( User user : users ) {
      if( user.getName().equals( name ) ) {
        return user;
      }
    }
    throw new NotFoundException( "User not found" );
  }

  @DELETE
  @Path( "/{username}" )
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( MediaType.APPLICATION_JSON )
  @ApiOperation( value = "Delete user", response = User.class )
  public User deleteUser( @PathParam( "username" ) String name ) {
    User user = null;
    for( User storedUser : users ) {
      if( storedUser.getName().equals( name ) ) {
        user = storedUser;
      }
    }
    if( user != null ) {
      users.remove( user );
      return user;
    }
    throw new NotFoundException( "User not found" );
  }

}
