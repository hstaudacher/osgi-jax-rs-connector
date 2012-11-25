package com.eclipsesource.jaxrs.consumer.internal;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.SslConfig;

// inspired by https://gist.github.com/1069465
public class ClientHelper {

  public static ClientConfig createClientConfig() {
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
    } catch( java.security.GeneralSecurityException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    ClientConfig config = new ClientConfig();
    HttpsURLConnection.setDefaultSSLSocketFactory( ctx.getSocketFactory() );
    try {
      HostnameVerifier verifier = new HostnameVerifier() {

        @Override
        public boolean verify( String hostname, SSLSession session ) {
          return true;
        }
      };
      SslConfig sslConfig = new SslConfig( verifier, ctx );
      config.setProperty( ClientProperties.SSL_CONFIG, sslConfig );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    return config;
  }
  
  private ClientHelper() {
    // prevent instantiation
  }
  
}