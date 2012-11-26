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
package com.eclipsesource.jaxrs.consumer.example;

import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;


public class Publisher {
  
  public void addConsumerPublisher( ConsumerPublisher publisher ) {
    String baseUrl = "https://api.github.com";
    publisher.publishConsumers( baseUrl, 
                                new Class<?>[] { GitHubUsers.class }, 
                                new Object[] {new GitHubUserProvider() } );
  }
}
