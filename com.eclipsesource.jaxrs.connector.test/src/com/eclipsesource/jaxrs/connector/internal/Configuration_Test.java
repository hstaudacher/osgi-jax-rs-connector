package com.eclipsesource.jaxrs.connector.internal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;


public class Configuration_Test {
  
  private JAXRSConnector connector;
  private Configuration config;

  @Before
  public void setUp() {
    connector = mock( JAXRSConnector.class );
    config = new Configuration( connector );
  }
  
  @Test
  public void testUpdateWithNull() throws Exception {
    config.updated( null );
    
    verify( connector, never() ).updatePath( anyString() );
  }
  
  @Test
  public void testUpdateWithPath() throws Exception {
    config.updated( createProperties( "/test" ) );
    
    verify( connector ).updatePath( "/test" );
  }
  
  @Test
  public void testUpdateWithPath2() throws Exception {
    config.updated( createProperties( "/test2" ) );
    
    verify( connector ).updatePath( "/test2" );
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
    Hashtable<String, Object> properties = new Hashtable<String, Object>();
    properties.put( Configuration.ROOT_PROPERTY, path );
    return properties;
  }
}
