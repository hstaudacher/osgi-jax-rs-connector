package com.eclipsesource.jaxrs.consumer;


public interface ConsumerPublisher {
  
  void publishConsumers( String baseUrl, Class<?>[] types, Object[] providers );
  
}
