/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.osgi.service.cm.ConfigurationException;


@SuppressWarnings( "deprecation" )
public class Configuration_Test {
  
  private JAXRSConnector connector;
  private Configuration config;

  @Before
  public void setUp() {
    connector = mock( JAXRSConnector.class );
    config = new Configuration( connector );
  }
  
  @Test
  public void testHasDefaultPublishDelay() {
    long publishDelay = config.getPublishDelay();
    
    assertEquals( 150L, publishDelay );
  }
  
  @Test
  public void testHasDefaultRootPath() {
    String roothPath = config.getRoothPath();
    
    assertEquals( "/services", roothPath );
  }
  
  @Test
  public void testHasDefaultDisableWadl() {
    boolean wadlDisabled = config.isWadlDisabled();
    
    assertFalse( wadlDisabled );
  }
  
  @Test
  public void testUpdateWithNull() throws Exception {
    config.updated( null );
    
    verify( connector, never() ).updateConfiguration( any( Configuration.class ) );
  }
  
  @Test
  
  public void testUpdateWithPath() throws Exception {
    config.updated( createProperties( "/test" ) );
    
    ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass( Configuration.class );
    verify( connector ).updateConfiguration( captor.capture() );
    assertEquals( "/test", captor.getValue().getRoothPath() );
    assertEquals( 4L, captor.getValue().getPublishDelay() );
    assertFalse( captor.getValue().isWadlDisabled() );
  }
  
  @Test
  public void testUpdateWithPath2() throws Exception {
    config.updated( createProperties( "/test2" ) );
    
    ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass( Configuration.class );
    verify( connector ).updateConfiguration( captor.capture() );
    assertEquals( "/test2", captor.getValue().getRoothPath() );
    assertEquals( 4L, captor.getValue().getPublishDelay() );
    assertFalse( captor.getValue().isWadlDisabled() );
  }
  
  @Test
  public void testUpdateWithDisabledWadl() throws Exception {
    config.updated( createProperties( "/test", true ) );
    
    ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass( Configuration.class );
    verify( connector ).updateConfiguration( captor.capture() );
    assertEquals( "/test", captor.getValue().getRoothPath() );
    assertEquals( 4L, captor.getValue().getPublishDelay() );
    assertTrue( captor.getValue().isWadlDisabled() );
  }
  
  @Test
  public void testUpdateWithPublishInterval() throws Exception {
    config.updated( createProperties( "/test", true ) );
    
    ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass( Configuration.class );
    verify( connector ).updateConfiguration( captor.capture() );
    assertEquals( "/test", captor.getValue().getRoothPath() );
    assertEquals( 4L, captor.getValue().getPublishDelay() );
    assertTrue( captor.getValue().isWadlDisabled() );
  }
  
  @Test( expected = ConfigurationException.class )
  public void testUpdateWithInvalidPath() throws Exception {
    config.updated( createProperties( "test" ) );
  }
  
  @Test( expected = ConfigurationException.class )
  public void testUpdateWithEmptyProperties() throws Exception {
    config.updated( new Hashtable<String, Object>() );
  }

  private Dictionary<String, ?> createProperties( String path ) {
    return createProperties( path, false);
  }
  
  private Dictionary<String, ?> createProperties( String path, Boolean disableWadl) {
    Hashtable<String, Object> properties = new Hashtable<String, Object>();
    properties.put( Configuration.PROPERTY_ROOT, path );
    properties.put( Configuration.PROPERTY_WADL_DISABLE, disableWadl );
    properties.put( Configuration.PROPERTY_PUBLISH_DELAY, 4L );
    return properties;
  }
  
}
