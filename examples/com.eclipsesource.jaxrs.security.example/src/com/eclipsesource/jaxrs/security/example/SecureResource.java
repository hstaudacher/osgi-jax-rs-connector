/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Holger Staudacher - API finalization
 ******************************************************************************/
package com.eclipsesource.jaxrs.security.example;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path( "/security" )
public class SecureResource {

  @GET
  @Path( "/unsecure" )
  public String getUnsecure() {
    return "This is the result of a request to the unsecured method";
  }

  @GET
  @Path( "/secure" )
  @RolesAllowed( "secure" )
  public String getSecure() {
    return "This is the result of a request to the secured method";
  }
}
