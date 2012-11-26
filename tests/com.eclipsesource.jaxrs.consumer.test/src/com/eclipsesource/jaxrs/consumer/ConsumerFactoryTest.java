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
package com.eclipsesource.jaxrs.consumer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.Test;


public class ConsumerFactoryTest {
  
  @Path( "/test" )
  public static class FakeResource {
    
    @GET
    public String get() {
      return "";
    }
    
  }
  
  @Path( "/test" )
  public static interface IFakeResource {
    @GET
    String get();
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithClass() {
    ConsumerFactory.createConsumer( "http://localhost", FakeResource.class );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullClass() {
    ConsumerFactory.createConsumer( "http://localhost", null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithInvalidUrl() {
    ConsumerFactory.createConsumer( "foo", IFakeResource.class );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUrl() {
    ConsumerFactory.createConsumer( null, IFakeResource.class );
  }
}
