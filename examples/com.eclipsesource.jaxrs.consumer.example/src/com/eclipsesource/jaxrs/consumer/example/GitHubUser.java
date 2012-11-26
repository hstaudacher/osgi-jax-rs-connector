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


public class GitHubUser {
  
  private final String userName;
  private final String url;
  private final String company;

  public GitHubUser( String userName, String url, String company ) {
    this.userName = userName;
    this.url = url;
    this.company = company;
  }
  
  public String getUserName() {
    return userName;
  }
  
  public String getUrl() {
    return url;
  }

  public String getCompany() {
    return company;
  }
  
  @Override
  public String toString() {
    return userName + " works for " + company + " (" + url + ")";
  }
  
  
}
