/*******************************************************************************
 * Copyright (c) 2012,2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// inspired by https://gist.github.com/1069465
public class ClientHelper {
  
  public static SSLContext createSSLContext() {
    TrustManager[] certs = new TrustManager[]{
      new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        @Override
        public void checkServerTrusted( X509Certificate[] chain, String authType )
          throws CertificateException
        {
          // no content
        }

        @Override
        public void checkClientTrusted( X509Certificate[] chain, String authType )
          throws CertificateException
        {
          // no content
        }
      }
    };
    SSLContext ctx = null;
    try {
      ctx = SSLContext.getInstance( "TLS" );
      ctx.init( null, certs, new SecureRandom() );
      return ctx;
    } catch( java.security.GeneralSecurityException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  public static HostnameVerifier createHostNameVerifier() {
    HostnameVerifier verifier = new HostnameVerifier() {

      @Override
      public boolean verify( String hostname, SSLSession session ) {
        return true;
      }
    };
    return verifier;
  }
  
  public static boolean hasFormAnnotation( Method method, Class<? extends Annotation> type ) {
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      for( Annotation annotation : annotations ) {
        if( annotation.annotationType() == type ) {
          return true;
        }
      }
    }
    return false;
  }
  
  private ClientHelper() {
    // prevent instantiation
  }

}