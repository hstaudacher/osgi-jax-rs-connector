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

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@Provider
public class RolesAllowedDynamicFeatureImpl extends RolesAllowedDynamicFeature {
  // We have to extend the Jersey RolesAllowedDynamicFeature so that the
  // @Provider annotation can be used and registered as an OSGi service.
}
