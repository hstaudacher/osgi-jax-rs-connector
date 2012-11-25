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
