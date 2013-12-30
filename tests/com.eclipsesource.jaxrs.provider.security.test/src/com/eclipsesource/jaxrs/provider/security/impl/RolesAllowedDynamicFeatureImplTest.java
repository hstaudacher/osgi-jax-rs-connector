/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.impl;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.ext.Provider;

import org.junit.Test;


public class RolesAllowedDynamicFeatureImplTest {
  
  @Test
  public void testHasProviderAnnotation() {
    boolean annotationPresent = RolesAllowedDynamicFeatureImpl.class.isAnnotationPresent( Provider.class );
    
    assertTrue( annotationPresent );
  }
}
