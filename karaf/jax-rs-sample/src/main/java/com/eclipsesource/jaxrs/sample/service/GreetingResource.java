package com.eclipsesource.jaxrs.sample.service;


import com.eclipsesource.jaxrs.sample.model.SimpleMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/greeting")
public interface GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SimpleMessage greeting();
}
