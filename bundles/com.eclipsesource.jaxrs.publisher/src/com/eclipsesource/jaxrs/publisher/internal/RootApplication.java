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
package com.eclipsesource.jaxrs.publisher.internal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;


public class RootApplication extends Application {
  private transient Map<String, Object> cachedProperties = new HashMap<String, Object>();
  
  private final List<Object> resources;

  public RootApplication() {
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

  @Override
  public Set<Object> getSingletons() {
    Set<Object> singletons = new HashSet<Object>( super.getSingletons() );
    singletons.addAll( resources );
    return singletons;
  }

    @Override
    public Map<String, Object> getProperties() {
        return cachedProperties;
    }

    public void addProperties( Map<String, Object> properties ) {
        cachedProperties.putAll(properties);
    }
  
}
