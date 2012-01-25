/*******************************************************************************
 * Copyright (c) 2012 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Schindl - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector;

import java.util.List;

import javax.xml.bind.JAXBContext;

/**
 * Service that allows to register classes in the {@link JAXBContext} of the application
 */
public interface ModelClassProvider {
  /**
   * @return list of classes to register for serialization
   */
  public List<Class<?>> getModelClasses();
}
