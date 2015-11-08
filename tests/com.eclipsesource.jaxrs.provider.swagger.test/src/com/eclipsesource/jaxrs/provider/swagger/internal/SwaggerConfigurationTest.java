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

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import io.swagger.models.Info;


public class SwaggerConfigurationTest {

  private SwaggerConfiguration configuration;
  private Dictionary<String, String> properties;

  @Before
  public void setUp() {
    configuration = new SwaggerConfiguration();
    properties = createProperties();
  }

  private Dictionary<String, String> createProperties() {
    Dictionary<String, String> properties = new Hashtable<>();
    properties.put( "swagger.host", "host" );
    properties.put( "swagger.basePath", "path" );
    properties.put( "swagger.filterClass", "filter" );
    properties.put( "swagger.info.title", "title" );
    properties.put( "swagger.info.description", "desc" );
    properties.put( "swagger.info.version", "version" );
    properties.put( "swagger.info.termsOfService", "terms" );
    properties.put( "swagger.info.contact.name", "name" );
    properties.put( "swagger.info.contact.email", "email" );
    properties.put( "swagger.info.contact.url", "url" );
    properties.put( "swagger.info.license.name", "licenseName" );
    properties.put( "swagger.info.license.url", "licenseUrl" );
    return properties;
  }

  @Test
  public void testGetsHost() throws ConfigurationException {
    configuration.updated( properties );

    String host = configuration.getHost();

    assertEquals( "host", host );
  }

  @Test
  public void testGetsBasePath() throws ConfigurationException {
    configuration.updated( properties );

    String basePath = configuration.getBasePath();

    assertEquals( "path", basePath );
  }

  @Test
  public void testGetsFilterClass() throws ConfigurationException {
    configuration.updated( properties );

    String filterClass = configuration.getFilterClass();

    assertEquals( "filter", filterClass );
  }

  @Test
  public void testGetsInfo() throws ConfigurationException {
    configuration.updated( properties );

    Info info = configuration.getInfo();

    assertEquals( "title", info.getTitle() );
    assertEquals( "version", info.getVersion() );
    assertEquals( "desc", info.getDescription() );
    assertEquals( "terms", info.getTermsOfService() );
    assertEquals( "name", info.getContact().getName() );
    assertEquals( "email", info.getContact().getEmail() );
    assertEquals( "url", info.getContact().getUrl() );
    assertEquals( "licenseName", info.getLicense().getName() );
    assertEquals( "licenseUrl", info.getLicense().getUrl() );
  }

}
