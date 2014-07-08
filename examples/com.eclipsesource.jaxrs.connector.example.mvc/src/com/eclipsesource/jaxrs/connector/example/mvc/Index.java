package com.eclipsesource.jaxrs.connector.example.mvc;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;


@Path( "index" )
public class Index {
  
  List<Item> items() {
    return Arrays.asList(
            new Item("Item 1", "$19.99", Arrays.asList(new Feature("New!"), new Feature("Awesome!"))),
            new Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new Feature("Ugly.")))
    );
  }

  static class Item {
    Item(String name, String price, List<Feature> features) {
      this.name = name;
      this.price = price;
      this.features = features;
    }

    String name, price;
    List<Feature> features;
  }

  static class Feature {
    Feature(String description) {
      this.description = description;
    }

    String description;
  }
  
  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/index.mustache" )
  public Index get() {
      return this;
  }
  
}
