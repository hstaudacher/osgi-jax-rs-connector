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
