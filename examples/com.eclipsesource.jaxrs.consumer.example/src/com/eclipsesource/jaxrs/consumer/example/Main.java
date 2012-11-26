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

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;


public class Main {
  
  public static void main( String[] args ) {
    String baseUrl = "https://api.github.com";
    GitHubUsers users = ConsumerFactory.createConsumer( baseUrl, 
                                                        GitHubUsers.class, 
                                                        new GitHubUserProvider() );
    System.out.println( users.getUser( "hstaudacher" ) );
  }
}
