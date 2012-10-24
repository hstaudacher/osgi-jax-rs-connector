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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces( "text/html" )
public class ProductProvider implements MessageBodyWriter<Product> {

  @Override
  public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return Product.class.isAssignableFrom( type );
  }

  @Override
  public long getSize( Product t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return -1;
  }

  @Override
  public void writeTo( Product product, 
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream entityStream ) throws IOException, WebApplicationException
  {
    PrintWriter writer = new PrintWriter( entityStream );
    writer.println( "<html>" );
    writer.println( "<body>" );
    writer.println( "Product name: " + product.getName() );
    writer.println( "<br/>" );
    writer.println( "Product Description :<br/>" + product.getDescription() );
    writer.println( "</body>" );
    writer.println( "</html>" );
    writer.flush();
  }
}
