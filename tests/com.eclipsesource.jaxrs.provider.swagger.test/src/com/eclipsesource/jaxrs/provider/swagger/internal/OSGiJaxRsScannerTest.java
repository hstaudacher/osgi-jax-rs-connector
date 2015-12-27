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
package com.eclipsesource.jaxrs.provider.swagger.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

import org.junit.Test;

import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.models.Info;
import io.swagger.models.Swagger;


public class OSGiJaxRsScannerTest {

  @Test
  public void testIsScanner() {
    assertTrue( new OSGiJaxRsScanner( mock( SwaggerConfiguration.class ) ) instanceof DefaultJaxrsScanner );
  }

  @Test
  public void testConfiguresSwagger() {
    SwaggerConfiguration swaggerConfiguration = mock( SwaggerConfiguration.class );
    when( swaggerConfiguration.getBasePath() ).thenReturn( "path" );
    when( swaggerConfiguration.getHost() ).thenReturn( "host" );
    when( swaggerConfiguration.getFilterClass() ).thenReturn( "filter" );
    Info info = mock( Info.class );
    when( swaggerConfiguration.getInfo() ).thenReturn( info );
    OSGiJaxRsScanner scanner = new OSGiJaxRsScanner( swaggerConfiguration );
    Swagger swagger = mock( Swagger.class );

    Swagger actualSwagger = scanner.configure( swagger );

    verify( swagger ).setBasePath( "path" );
    verify( swagger ).setHost( "host" );
    verify( swagger ).setInfo( info );
    assertEquals( scanner.getFilterClass(), "filter" );
    assertSame( swagger, actualSwagger );
  }


  @Test
  public void testAddsIntefacesToClasses() {
    Application application = mock( Application.class );
    Set<Object> singletons = new HashSet<>();
    singletons.add( new TestResource() );
    when( application.getSingletons() ).thenReturn( singletons );
    OSGiJaxRsScanner scanner = new OSGiJaxRsScanner( null );

    Set<Class<?>> classes = scanner.classesFromContext( application, mock( ServletConfig.class ) );

    assertTrue( classes.contains( TestResource.class ) );
    assertTrue( classes.contains( TestInterface.class ) );
  }

  private interface TestInterface {
    // no content
  }

  private static class TestResource implements TestInterface {

    public TestResource() {
    }

  }

}
