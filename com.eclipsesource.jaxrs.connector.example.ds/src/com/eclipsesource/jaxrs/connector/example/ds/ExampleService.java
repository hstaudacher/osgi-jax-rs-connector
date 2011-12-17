package com.eclipsesource.jaxrs.connector.example.ds;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path( value = "/test" )
public class ExampleService {
  
  @GET
  public String seyHello() {
    return "it works. JAX-RS is integrated well with OSGi.";
  }
}
