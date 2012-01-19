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
package com.eclipsesource.jaxrs.connector.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sun.jersey.api.core.DefaultResourceConfig;


public class Application extends DefaultResourceConfig {
  
  private final List<Object> resources;

  public Application() {
    resources = new LinkedList<Object>();
  }
  
  void addResource( Object resource ) {
    resources.add( resource );
  }
  
  void removeResource( Object resource ) {
    resources.remove( resource );
  }
  
  boolean hasResources() {
    return !resources.isEmpty();
  }
  
  public List<Object> getResources() {
    return new LinkedList<Object>( resources );
  }

  @Override
  public Set<Object> getSingletons() {
    Set<Object> singletons = super.getSingletons();
    singletons.addAll( resources );
    return singletons;
  }
  
}
