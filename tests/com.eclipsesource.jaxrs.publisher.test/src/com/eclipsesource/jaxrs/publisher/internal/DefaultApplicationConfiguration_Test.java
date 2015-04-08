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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.Before;
import org.junit.Test;


public class DefaultApplicationConfiguration_Test {
  
  private DefaultApplicationConfiguration appConfig;

  @Before
  public void setUp() {
    appConfig = new DefaultApplicationConfiguration( createConfiguration( "/foo", true, 23L ) );
  }
  
  @Test
  public void testEnablesMetaInfLookup() {
    Map<String, Object> properties = appConfig.getProperties();
    
    Object value = properties.get( ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE );
    
    assertEquals( Boolean.FALSE, value );
  }
  
  @Test
  public void testDisablesAutoDiscovery() {
    Map<String, Object> properties = appConfig.getProperties();
    
    Object value = properties.get( ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE );
    
    assertEquals( Boolean.TRUE, value );
  }
  
  @Test
  public void testHasWadlDisabled() {
    Map<String, Object> properties = appConfig.getProperties();
    
    Object value = properties.get( ServerProperties.WADL_FEATURE_DISABLE );
    
    assertEquals( Boolean.TRUE, value );
  }
  
  @SuppressWarnings( "deprecation" )
  private Configuration createConfiguration( String path, boolean wadlDisable, long publishDelay ) {
    Configuration configuration = mock( Configuration.class );
    when( configuration.getRoothPath() ).thenReturn( path );
    when( configuration.getPublishDelay() ).thenReturn( publishDelay );
    when( configuration.isWadlDisabled() ).thenReturn( wadlDisable );
    return configuration;
  }
}
