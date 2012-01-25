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

import java.util.WeakHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.jaxrs.connector.ModelClassProvider;

public class ModelClassProviderTracker extends ServiceTracker<ModelClassProvider, ModelClassProvider> {

  private WeakHashMap<DelegatingJAXBContextProvider, Boolean> delegatesMap = new WeakHashMap<DelegatingJAXBContextProvider, Boolean>();

  public ModelClassProviderTracker( BundleContext context ) {
    super( context, ModelClassProvider.class.getName(), null );
  }

  public void addDelegatingJAXBContextProvider( DelegatingJAXBContextProvider delegate ) {
    synchronized( delegatesMap ) {
      if( !delegatesMap.containsKey( delegate ) ) {
        delegatesMap.put( delegate, Boolean.TRUE );
        for( ModelClassProvider provider : getServices( new ModelClassProvider[ 0 ] ) ) {
          delegate.registerClasses( provider.getModelClasses() );
        }
      }
    }
  }

  @Override
  public ModelClassProvider addingService( ServiceReference<ModelClassProvider> reference ) {
    ModelClassProvider provider = getService( reference );
    DelegatingJAXBContextProvider[] delegates;
    synchronized( delegatesMap ) {
      delegates = delegatesMap.keySet().toArray( new DelegatingJAXBContextProvider[ 0 ] );
    }
    for( DelegatingJAXBContextProvider p : delegates ) {
      p.registerClasses( provider.getModelClasses() );
    }
    return super.addingService( reference );
  }

  @Override
  public void removedService( ServiceReference<ModelClassProvider> reference,
                              ModelClassProvider service )
  {
    DelegatingJAXBContextProvider[] delegates;
    synchronized( delegatesMap ) {
      delegates = delegatesMap.keySet().toArray( new DelegatingJAXBContextProvider[ 0 ] );
    }
    for( DelegatingJAXBContextProvider p : delegates ) {
      p.unregisterClasses( service.getModelClasses() );
    }
    super.removedService( reference, service );
  }
}
