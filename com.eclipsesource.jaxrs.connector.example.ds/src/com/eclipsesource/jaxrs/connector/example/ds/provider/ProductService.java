/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dirk Lecluse - initial API and implementation
 *    Holger Staudacher - ongoing development
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.example.ds.provider;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path( "/product" )
public class ProductService {
	
  private static Map<String, Product> simplisticProductDb = new HashMap<String, Product>();
  
  static {
    simplisticProductDb.put( "1", new Product( "Pencil", "Simple writing instrument" ) );
    simplisticProductDb.put( "2", new Product( "Roller", "Standard writing instrument" ) );
    simplisticProductDb.put( "3", new Product( "Foutain Pen", "Stylish writing instrument" ) );
  }

  @GET
  @Path( "/{id}" )
  public Product seyHello( @PathParam( "id" ) String id ) {
    Product someProduct = simplisticProductDb.get( id );
    return someProduct;
  }
}
