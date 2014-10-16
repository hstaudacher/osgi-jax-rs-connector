package com.eclipsesource.jaxrs.sample.service.impl;


import com.eclipsesource.jaxrs.sample.model.SimpleMessage;
import com.eclipsesource.jaxrs.sample.service.GreetingResource;

public class SimpleResource implements GreetingResource {

    @Override
    public SimpleMessage greeting() {
        return new SimpleMessage(System.currentTimeMillis(), "Hello this is current time");
    }
}
