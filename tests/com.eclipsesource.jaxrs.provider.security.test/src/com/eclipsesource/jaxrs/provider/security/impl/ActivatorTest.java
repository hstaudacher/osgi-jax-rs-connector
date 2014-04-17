/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Dictionary;

import org.junit.Test;
import org.osgi.framework.BundleContext;


public class ActivatorTest {
  
  @Test
  public void testSetsInstance() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );

    assertSame( activator, Activator.getInstance() );
  }
  
  @Test
  @SuppressWarnings( "unchecked" )
  public void testRegistersRolesAllowedDynamicFeature() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );
    
    verify( context ).registerService( eq( RolesAllowedDynamicFeatureImpl.class.getName() ), 
                                       any( RolesAllowedDynamicFeatureImpl.class ), 
                                       any( Dictionary.class ) );
  }
  
  @Test
  @SuppressWarnings( "unchecked" )
  public void testRegistersContainerRequestFilter() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );
    
    verify( context ).registerService( eq( ContainerRequestFilterImpl.class.getName() ), 
                                       any( ContainerRequestFilterImpl.class ), 
                                       any( Dictionary.class ) );
  }
}
