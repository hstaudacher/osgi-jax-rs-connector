/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.security.fixture;

import java.security.Principal;

public class User implements Principal {

  private final String name;

  public User( String name ) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
