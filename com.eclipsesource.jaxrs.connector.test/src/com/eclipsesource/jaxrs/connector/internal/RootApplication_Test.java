package com.eclipsesource.jaxrs.connector.internal;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.InvalidSyntaxException;


public class RootApplication_Test {
  
  private RootApplication application;

  @Before
  public void setUp() {
    RootApplication original = new RootApplication();
    application = spy( original );
  }
  
  @Test
  public void testGetSingletons() throws InvalidSyntaxException {
    Object resource = mock( Object.class );
    application.addResource( resource );
    
    Set<Object> services = application.getSingletons();
    
    assertEquals( 1, services.size() );
    assertTrue( services.contains( resource ) );
  }
  
  @Test
  public void testremoveResourceGetSingletons() throws InvalidSyntaxException {
    Object resource = mock( Object.class );
    application.addResource( resource );
    application.removeResource( resource );
    
    Set<Object> services = application.getSingletons();
    
    assertEquals( 0, services.size() );
  }
  
  @Test
  public void testHasResources() throws InvalidSyntaxException {
    Object resource = mock( Object.class );
    application.addResource( resource );
    
    assertTrue( application.hasResources() );
  }
  
  @Test
  public void testGetResources() {
    Object resource1 = mock( Object.class );
    application.addResource( resource1 );
    Object resource2 = mock( Object.class );
    application.addResource( resource2 );
    
    List<Object> resources = application.getResources();
    assertEquals( 2, resources.size() );
    assertTrue( resources.contains( resource1 ) );
    assertTrue( resources.contains( resource2 ) );
  }
  
  @Test
  public void testGetResourcesIsSaveCopy() {
    Object resource1 = mock( Object.class );
    application.addResource( resource1 );
    Object resource2 = mock( Object.class );
    application.addResource( resource2 );

    List<Object> resources = application.getResources();
    application.removeResource( resource1 );
    
    assertEquals( 2, resources.size() );
  }
  
  @Test
  public void testHasNoResources() throws InvalidSyntaxException {
    assertFalse( application.hasResources() );
  }
  
  @Test
  public void testFindsNoResourceServices() throws InvalidSyntaxException {
    Set<Object> classes = application.getSingletons();
    
    assertEquals( 0, classes.size() );
  }
  
}
