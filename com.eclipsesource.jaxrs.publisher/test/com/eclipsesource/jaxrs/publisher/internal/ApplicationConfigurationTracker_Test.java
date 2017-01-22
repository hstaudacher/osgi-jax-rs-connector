/*******************************************************************************
 * Copyright (c) 2015 Holger Staudacher and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.publisher.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.eclipsesource.jaxrs.publisher.ApplicationConfiguration;


@RunWith( MockitoJUnitRunner.class )
public class ApplicationConfigurationTracker_Test {
  
  private ApplicationConfigurationTracker tracker;
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference reference;

  @Before
  public void setUp() {
    BundleContext context = mock( BundleContext.class );
    tracker = new ApplicationConfigurationTracker( context, connector );
  }
  
  @Test
  public void delegatesAddAppConfig() {
    tracker.addingService( reference );
    
    verify( connector ).addApplicationConfiguration( reference );
  }
  
  @Test
  public void delegatesRemoveAppConfig() {
    ApplicationConfiguration service = mock( ApplicationConfiguration.class );
    
    tracker.removedService( reference, service );
    
    verify( connector ).removeApplicationConfiguration( reference, service );
  }
}
