/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 *    ProSyst Software GmbH. - compatibility with OSGi specification 4.2 APIs
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.sse;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class ActivatorTest {

  @Test
  @SuppressWarnings( "unchecked" )
  public void testRegistersSseProviderOnStart() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    
    activator.start( context );
    
    verify( context ).registerService( eq( SseFeature.class.getName() ), any( SseFeature.class ), any( Dictionary.class ) );
  }
  
  @Test
  @SuppressWarnings( "unchecked" )
  public void testUnregistersSseProviderOnStop() throws Exception {
    Activator activator = new Activator();
    BundleContext context = mock( BundleContext.class );
    ServiceRegistration registration = mock( ServiceRegistration.class );
    when( context.registerService( any( String.class ), any( Object.class ), any( Dictionary.class ) ) ).thenReturn( registration );
    activator.start( context );
    
    activator.stop( context );
    
    verify( registration ).unregister();
  }
}
