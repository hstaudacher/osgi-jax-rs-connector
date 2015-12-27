package com.eclipsesource.jaxrs.connector.example.swagger;


public class MetaInfoService implements MetaInfo {

  @Override
  public String getInfo() {
    return "meta";
  }

}
