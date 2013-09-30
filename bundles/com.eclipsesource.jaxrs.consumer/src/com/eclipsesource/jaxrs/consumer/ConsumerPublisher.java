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
package com.eclipsesource.jaxrs.consumer;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;


/**
 * <p>
 * When running in an OSGi environment this interface will be registered as OSGi service. It's usage is for
 * creating consumer objects out of <code>@Path</code> interfaces and register them as OSGi services too.
 * </p>
 * 
 * @see ConsumerFactory
 * @see Path
 * @see Provider
 */
public interface ConsumerPublisher {
  
  /**
   * <p>
   * Creates consumer objets. See the {@link ConsumerFactory} for a detailed description. When the consumers
   * are created they will be registered as OSGi services and can be consumed from other bundles.
   * </p>
   * @see ConsumerFactory
   */
  void publishConsumers( String baseUrl, Class<?>[] types, Object[] providers );
  
}
