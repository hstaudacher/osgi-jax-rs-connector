/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Holger Staudacher - API finalization
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Priority( Priorities.AUTHENTICATION )
public class ContainerRequestFilterImpl implements ContainerRequestFilter {
  
  private final SecurityAdmin securityAdmin;

  public ContainerRequestFilterImpl( SecurityAdmin securityAdmin ) {
    this.securityAdmin = securityAdmin;
  }

  @Override
  public void filter( ContainerRequestContext requestContext ) throws IOException {
    if( securityAdmin != null ) {
      SecurityContext securityContext = securityAdmin.getSecurityContext( requestContext );
      if( securityContext != null ) {
        requestContext.setSecurityContext( securityContext );
      }
    }
  }
}
