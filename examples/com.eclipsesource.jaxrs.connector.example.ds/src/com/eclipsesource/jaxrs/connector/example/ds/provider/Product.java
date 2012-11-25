/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Dirk Lecluse - initial API and
 * implementation Holger Staudacher - ongoing development
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.example.ds.provider;

public class Product {

  private String Name;
  private String Description;

  public Product( String name, String description ) {
    super();
    Name = name;
    Description = description;
  }

  public String getName() {
    return Name;
  }

  public void setName( String name ) {
    Name = name;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription( String description ) {
    Description = description;
  }
}
