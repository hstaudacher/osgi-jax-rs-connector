package com.eclipsesource.jaxrs.connector.internal;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sun.jersey.api.core.DefaultResourceConfig;


public class RootApplication extends DefaultResourceConfig {
  
  private List<Object> resourcePool;

  public RootApplication() {
    resourcePool = new LinkedList<Object>();
  }
  
  void addResource( Object resource ) {
    resourcePool.add( resource );
  }
  
  void removeResource( Object resource ) {
    resourcePool.remove( resource );
  }
  
  boolean hasResources() {
    return !resourcePool.isEmpty();
  }
  
  public List<Object> getResources() {
    return new LinkedList<Object>( resourcePool );
  }

  @Override
  public Set<Object> getSingletons() {
    Set<Object> singletons = super.getSingletons();
    singletons.addAll( resourcePool );
    return singletons;
  }
  
}
