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

import org.glassfish.jersey.client.ClientConfig;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;


public class Main {
  
  public static void main( String[] args ) {
    String baseUrl = "https://api.github.com";
    ClientConfig config = new ClientConfig().register( new GitHubUserProvider() );
    GitHubUsers users = ConsumerFactory.createConsumer( baseUrl,
                                                        config,
                                                        GitHubUsers.class );
    System.out.println( users.getUser( "hstaudacher" ) );
  }
}
