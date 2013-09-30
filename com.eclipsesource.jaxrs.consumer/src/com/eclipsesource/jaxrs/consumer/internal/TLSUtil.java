package com.eclipsesource.jaxrs.consumer.internal;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class TLSUtil {

  public static void initializeUntrustedContext() {
    X509TrustManager tm = new X509TrustManager() {

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      @Override
      public void checkServerTrusted( X509Certificate[] paramArrayOfX509Certificate,
                                      String paramString ) throws CertificateException
      {
        // no content
      }

      @Override
      public void checkClientTrusted( X509Certificate[] paramArrayOfX509Certificate,
                                      String paramString ) throws CertificateException
      {
        // no content
      }
    };
    SSLContext ctx;
    try {
      ctx = SSLContext.getInstance( "TLS" );
      ctx.init( null, new TrustManager[] { tm }, null );
      SSLContext.setDefault( ctx );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier() {

      @Override
      public boolean verify( String hostname, SSLSession session ) {
        return true;
      }
    } );
  }
}
