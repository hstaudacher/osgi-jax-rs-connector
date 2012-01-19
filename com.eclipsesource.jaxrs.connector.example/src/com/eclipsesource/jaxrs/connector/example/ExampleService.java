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
package com.eclipsesource.jaxrs.connector.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path( value = "/test" )
public class ExampleService {
  
  @GET
  public String seyHello() {
    return "it works. JAX-RS is integrated well with OSGi.";
  }
}
