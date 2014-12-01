package com.eclipsesource.jaxrs.sample.consumer;

import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;
import com.eclipsesource.jaxrs.sample.service.GreetingResource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

@Component(immediate = true)
@Slf4j
public class PublishConsumerService {

    @Getter
    private ConsumerPublisher publisher;

    @Activate
    public void activate(Map<String, Object> config) {
        log.info("Publishing jax-rs consumer");
        publisher.publishConsumers("http://localhost:8181/services",
                new Class<?>[]{GreetingResource.class},
                new Object[]{});
    }

    @Reference(service = ConsumerPublisher.class, unbind = "unSetPublisher")
    public void setPublisher(ConsumerPublisher publisher) {
        this.publisher = publisher;
    }

    protected void unSetPublisher(ConsumerPublisher consumerPublisher) {
        this.publisher = null;
    }
}
