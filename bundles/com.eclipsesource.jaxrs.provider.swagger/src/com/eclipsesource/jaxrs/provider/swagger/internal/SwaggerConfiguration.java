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

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;

public class SwaggerConfiguration implements ManagedService {

  public static final String SERVICE_PID = "com.eclipsesource.jaxrs.swagger.config";

  static final String SWAGGER_PREFIX = "swagger.";
  static final String INFO_PREFIX = SWAGGER_PREFIX + "info.";

  static final String PROPERTY_BASE_PATH = SWAGGER_PREFIX + "basePath";
  static final String PROPERTY_HOST = SWAGGER_PREFIX + "host";
  static final String PROPERTY_FILTER_CLASS = SWAGGER_PREFIX + "filterClass";
  static final String PROPERTY_INFO_TITLE = INFO_PREFIX + "title";
  static final String PROPERTY_INFO_DESCRIPTION = INFO_PREFIX + "description";
  static final String PROPERTY_INFO_VERSION = INFO_PREFIX + "version";
  static final String PROPERTY_INFO_TERMS = INFO_PREFIX + "termsOfService";
  static final String PROPERTY_INFO_CONTACT_NAME = INFO_PREFIX + "contact.name";
  static final String PROPERTY_INFO_CONTACT_URL = INFO_PREFIX + "contact.url";
  static final String PROPERTY_INFO_CONTACT_EMAIL = INFO_PREFIX + "contact.email";
  static final String PROPERTY_INFO_LICENSE_NAME = INFO_PREFIX + "license.name";
  static final String PROPERTY_INFO_LICENSE_URL = INFO_PREFIX + "license.url";

  private static final String[] INFO_KEYS = new String[] {
    PROPERTY_INFO_TITLE,
    PROPERTY_INFO_DESCRIPTION,
    PROPERTY_INFO_VERSION,
    PROPERTY_INFO_TERMS,
    PROPERTY_INFO_CONTACT_NAME,
    PROPERTY_INFO_CONTACT_URL,
    PROPERTY_INFO_CONTACT_EMAIL,
    PROPERTY_INFO_LICENSE_NAME,
    PROPERTY_INFO_LICENSE_URL
  };

  private Dictionary<String, ?> configuration;

  @Override
  public void updated( Dictionary<String, ?> configuration ) throws ConfigurationException {
    this.configuration = configuration;
  }

  public Info getInfo() {
    if( hasInfoValues() ) {
      return buildInfo();
    }
    return null;
  }

  private boolean hasInfoValues() {
    for( String infoKey : INFO_KEYS ) {
      if( hasConfig( infoKey ) ) {
        return true;
      }
    }
    return false;
  }

  private Info buildInfo() {
    Info info = new Info();
    info.setTitle( getConfig( PROPERTY_INFO_TITLE ) );
    info.setDescription( getConfig( PROPERTY_INFO_DESCRIPTION ) );
    info.setVersion( getConfig( PROPERTY_INFO_VERSION ) );
    info.setTermsOfService( getConfig( PROPERTY_INFO_TERMS ) );
    info.setContact( buildContact() );
    info.setLicense( buildLicense() );
    return info;
  }

  private Contact buildContact() {
    if( hasConfig( PROPERTY_INFO_CONTACT_NAME )
        || hasConfig( PROPERTY_INFO_CONTACT_URL )
        || hasConfig( PROPERTY_INFO_CONTACT_EMAIL ) )
    {
      Contact contact = new Contact();
      contact.setEmail( getConfig( PROPERTY_INFO_CONTACT_EMAIL ) );
      contact.setName( getConfig( PROPERTY_INFO_CONTACT_NAME ) );
      contact.setUrl( getConfig( PROPERTY_INFO_CONTACT_URL ) );
      return contact;
    }
    return null;
  }

  private License buildLicense() {
    if( hasConfig( PROPERTY_INFO_LICENSE_NAME ) || hasConfig( PROPERTY_INFO_LICENSE_URL ) ) {
      License license = new License();
      license.setName( getConfig( PROPERTY_INFO_LICENSE_NAME ) );
      license.setUrl( getConfig( PROPERTY_INFO_LICENSE_URL ) );
      return license;
    }
    return null;
  }

  public String getBasePath() {
    return getConfig( PROPERTY_BASE_PATH );
  }

  public String getHost() {
    return getConfig( PROPERTY_HOST );
  }

  public String getFilterClass() {
    return getConfig( PROPERTY_FILTER_CLASS );
  }

  private String getConfig( String key ) {
    if( hasConfig( key ) ) {
      return ( String )configuration.get( key );
    }
    return null;
  }

  private boolean hasConfig( String key ) {
    if( configuration != null ) {
      return configuration.get( key ) != null;
    }
    return false;
  }

}
