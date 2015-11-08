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

import io.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.models.Swagger;

public class OSGiJaxRsScanner extends DefaultJaxrsScanner implements SwaggerConfig {

  private final SwaggerConfiguration swaggerConfiguration;

  public OSGiJaxRsScanner( SwaggerConfiguration swaggerConfiguration ) {
    this.swaggerConfiguration = swaggerConfiguration;
  }

  @Override
  public Swagger configure( Swagger swagger ) {
    swagger.setInfo( swaggerConfiguration.getInfo() );
    swagger.setBasePath( swaggerConfiguration.getBasePath() );
    swagger.setHost( swaggerConfiguration.getHost() );
    return swagger;
  }

  @Override
  public String getFilterClass() {
    return swaggerConfiguration.getFilterClass();
  }

}
