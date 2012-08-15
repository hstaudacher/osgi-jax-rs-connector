/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.eclipsesource.jaxrs.connector.internal.Activator_Test;
import com.eclipsesource.jaxrs.connector.internal.Configuration_Test;
import com.eclipsesource.jaxrs.connector.internal.HttpTracker_Test;
import com.eclipsesource.jaxrs.connector.internal.JAXRSConnector_Test;
import com.eclipsesource.jaxrs.connector.internal.JerseyContext_Test;
import com.eclipsesource.jaxrs.connector.internal.ResourceTracker_Test;
import com.eclipsesource.jaxrs.connector.internal.RootApplication_Test;
import com.eclipsesource.jaxrs.connector.internal.ServiceContainer_Test;


@RunWith( Suite.class )
@SuiteClasses( {
  JAXRSConnector_Test.class,
  RootApplication_Test.class,
  Activator_Test.class,
  ServiceContainer_Test.class,
  HttpTracker_Test.class,
  ResourceTracker_Test.class,
  JerseyContext_Test.class,
  Configuration_Test.class
} )
public class AllConnectorTestSuite {
  // no content
}
