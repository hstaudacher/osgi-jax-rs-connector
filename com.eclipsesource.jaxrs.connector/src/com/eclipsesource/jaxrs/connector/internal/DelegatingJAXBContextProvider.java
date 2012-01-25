/*******************************************************************************
 * Copyright (c) 2012 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Schindl - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.internal;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
public class DelegatingJAXBContextProvider implements ContextResolver<JAXBContext> {

  private JAXBContext ctx;
  private List<Class<?>> registeredClasses = new ArrayList<Class<?>>();

  public DelegatingJAXBContextProvider() {
    System.err.println("CREATING INSTANCE ....");
    Activator.INSTANCE.registerJaxbcontextProvider( this );
  }

  public void registerClasses( List<Class<?>> clazzes ) {
    System.err.println("=======================================> Registering classes: " + clazzes);
    synchronized( registeredClasses ) {
      registeredClasses.addAll( clazzes );
      if( ctx != null ) {
        ctx = null;
      }
    }
  }

  public void unregisterClasses( List<Class<?>> clazzes ) {
    synchronized( registeredClasses ) {
      registeredClasses.removeAll( clazzes );
      if( ctx != null ) {
        ctx = null;
      }
    }
  }

  @Override
  public JAXBContext getContext( Class<?> arg0 ) {
    synchronized( registeredClasses ) {
      if( ctx != null ) {
        return ctx;
      }
      try {
        Class<?>[] clazzes = new Class<?>[ registeredClasses.size() ];
        registeredClasses.toArray( clazzes );
        ctx = JAXBContext.newInstance( clazzes );
        return ctx;
      } catch( JAXBException e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
    }
  }
}
