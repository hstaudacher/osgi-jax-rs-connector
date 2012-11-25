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
