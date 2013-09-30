# OSGi - JAX-RS Connector 3.1.0 [![Build Status](https://travis-ci.org/hstaudacher/osgi-jax-rs-connector.png)](https://travis-ci.org/hstaudacher/osgi-jax-rs-connector)
[JAX-RS (JSR 311)](http://jsr311.java.net/) is the community-driven Standard for 
building RESTful web services with Java. The reference implementation for JAX-RS is 
[Jersey](http://jersey.java.net/) and ships as OSGi bundles. 
This project connects Jersey and OSGi at the service level. This means that OSGi services can be published as 
RESTful web services by simply registering them as OSGi services and also consumed as OSGi services ;).  

*To see how to get started with JAX-RS 2.0 and Jersey please read the [Jersey getting started guide](https://jersey.java.net/documentation/latest/getting-started.html).*

## Features
The OSGi-JAX-RS Connector provides **two bundles**. A **publisher** and a **consumer**. Both can be used completely separately or together, it's up to you. Additional the connector provides custom `@Provider` implementations that can be used optionally.

### Publisher

The publisher is located in the bundle `com.eclipsesource.jaxrs.publisher`. All it does is tracking up services. When it sports a service that is annotated with the JAX-RS annotations `@Path` or `@Provider` the work begins. The publisher hooks these service into Jersey and the OSGi HTTPService. Basically this means it publishes them as RESTful web services. It's just that simple.

By default the publisher registers the services using the context path `/services`. This means an OSGi service that is annotated with `@Path( "/foo" )` will be available using the path `/services/foo`. This context path is configurable using the OSGi configuration admin. You can condigure the service usign the service.pid `com.eclipsesource.jaxrs.connector` and the property `root` to define a custom path.

As said earlier, the publisher uses the OSGi HTTPService to publish the services. As a result all configuration topics regarding ports, protocol and so on are up to the HTTPService implementation of your choice.

If it's your wish to publish services on different ports, just register them and add the service property `http.port` with the port of your choice. Of course it's necessary that an HTTPService is up and running on such a port.

### Consumer
The consumer is lcoated in the bundle `com.eclipsesource.jaxrs.consumer`. The idea of the consumer is to reuse your `@Path` and `@Provider` interfaces for calling a service. From a technical point of view it takes your interfaces together with a base url and creates Java Proxies. These proxies will make an HTTP call when a method will be invoked on it. The proxy knows which method, parameters and so on it should use because you have it defined with the JAX-RS annotations. The consumer uses the JAX-RS 2.0 client API to send requests. So, there will be no additional dependencies.

A nice side effect of the consumer is, that it does not need OSGi. It's just a jar that can be used to create the mentioned proxies. See the [ConsumerFactory](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/bundles/com.eclipsesource.jaxrs.consumer/src/com/eclipsesource/jaxrs/consumer/ConsumerFactory.java) for more information.

When using it together with OSGi it provides a helper to create your proxies and automatically register them as OSGi services. But it's up to you if you want to take care regarding the publishing by your own. See the [ConsumerPublisher](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/bundles/com.eclipsesource.jaxrs.consumer/src/com/eclipsesource/jaxrs/consumer/ConsumerPublisher.java) for more information.

A detailed explenation of the concepts of the consumer together with some examples can be found on our blog: [Consuming REST services in Java the cool way](http://eclipsesource.com/blogs/2012/11/27/consuming-rest-services-in-java-the-cool-way/), [Consuming REST services in OSGi the cool way](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/).

### Providers
The custom `@Provider` implementations are located in their own feature. The following providers are currently included.
* `com.eclipsesource.jaxrs.provider.moxy` - Allows the de/serialization using [EclipseLink MOXy](http://www.eclipse.org/eclipselink/moxy.php).  
* `com.eclipsesource.jaxrs.provider.gson` - Allows the de/serialization using [Gson](https://code.google.com/p/google-gson/).  

## Installation
To ease the installation we provide a p2 repository and we publish the connector to maven central.

### p2
Install from this software repository into your target: `http://hstaudacher.github.io/osgi-jax-rs-connector`

**Please note:** If dependencies can't be satisfied please disable "include required software" within the target editor.

### Maven
**Publisher:**  
```
<dependency>
    <groupId>com.eclipsesource</groupId>
    <artifactId>osgi-jaxrs-connector</artifactId>
    <version>3.1.0</version>
</dependency>
```

**Consumer:**  
```
<dependency>
    <groupId>com.eclipsesource</groupId>
    <artifactId>jaxrs-consumer</artifactId>
    <version>2.1.0</version>
</dependency>
```

***Please note:*** Due to a renaming in version 3.0 the publisher is still called `connector` within maven central. 


## Usage
Basically all you need to to is add the publisher and/or the consumer to your OSGi container and start them. For people new to OSGi the steps to get started with the connector and Eclipse are described below.

1. Add the com.eclipsesource.jaxrs.connector.feature (OSGi JAX-RS Connector) to your target using the url mentioned in the Installation/p2 section.
2. Add the `com.eclipsesource.jaxrs.publisher` and its' dependencies bundles to your OSGi instance.
3. Convert some OSGi service to resources like in [this tutorial](http://jersey.java.net/nonav/documentation/latest/getting-started.html#d4e45)
4. Point your client to the specified url. Don't forget that the default root path is `/services`. So registering a 
service with the path `/example` would lead to `/services/example`.

The steps how the consumer will be handled are dscribed in this [post](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/).

## Examples
Examples for the publisher and consumer can be found within the [examples](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples) folder. 

### Publisher Examples
Two examples exist for the publisher. They are located in the bundles `com.eclipsesource.jaxrs.connector.example` 
and `com.eclipsesource.jaxrs.connector.example.ds`. As the names say one uses Declarative services and the other don't.
In both exampels a simple POJO is annotated with `@Path` and will be registered as an OSGIi service. Both bundles contain an Eclipse Launch Configuration to start them (please don't forget to add the publisher bundle to the launch config). After starting the launch configs the service will be available at `http://localhost:9090/services/osgi-jax-rs`.

As a bonus the `com.eclipsesource.jaxrs.connector.example.ds` contains an example how to use the `@Provider` together with the publisher.

### Consumer Examples
The example for the consumer is splitted into two bundles called `com.eclipsesource.jaxrs.consumer.example` and `com.eclipsesource.jaxrs.consumer.example.caller`. The example bundle contains the data model and the resource interface while the caller just calls the service. The example fetches data from github and is prett simple. 

## Requirements
* OSGi Core Specification 4.2 and an OSGi HttpService implementation (e.g. Equinox, Felix).
* JRE 1.6 (same as Jersey)

## Alternatives
* [Neil Bartlett's jaxrs-osgi-extender](https://github.com/njbartlett/jaxrs-osgi-extender)
* OSGi Remote Services (e.g. [Apache CXF](http://cxf.apache.org/distributed-osgi-reference.html#DistributedOSGiReference-ServiceProviderpropertiesForConfiguringRESTfulJAXRSbasedendpointsandconsumers))

## Jersey version
Jersey 2.3.1 + dependencies are included in the p2 software repository.  

## Changelog
* **1.0 (Jan 23rd, 2012):** Functionality to publish `@Path` annotated OSGi services as web resources.
* **2.0 (Aug 27th, 2012):** Use the config admin to specify the root path. The default root path is /services. You can configure the path by specifying a config with the service.pid `com.eclipsesource.jaxrs.connector` and the property `root`. The path needs to be a valid servlet path e.g. "/api".  
* **2.1 (Oct 26th, 2012):** Besides `@Path` annotated object it's now possible to register `@Provider` annotated objects as OSGi services too. Thanks to Dirk Lecluse for this contribution.
* **2.1.1 (Nov 11th, 2012):** Updated included Jersey Version (1.15).
* **2.2.0 (Jan 3rd, 2013):** Updated included Jersey Version (2.0 m11). Added Consumer integration.
* **2.3.0 (Jan 22th, 2013):** Added service property to exclude a resource from publishing. See [ServiceProperties](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/com.eclipsesource.jaxrs.publisher/src/com/eclipsesource/jaxrs/publisher/ServiceProperties.java).
* **2.4.0 (Feb 28th, 2013):** Updated included Jersey Version (2.0 m12).
* **2.4.1 (Mar 3rd, 2013):** Adds error handling to consumer.
* **3.0.0 (Jun 17th, 2013):** Updates Jersey version to 2.0 (release). Renames connector bundle to consumer.
* **3.1.0 (Sep 30th, 2013):** Updates Jersey version to 2.3.1 (release). Reduces JRE level to Java6 (#12). Adds custom providers for MOXy (#15, thanks @BryanHunt) and Gson. Adds metatype for configuring the base URL (#14, thanks @BryanHunt). Adds API to consumer to be able to pass in custom client configuration (#13). Restructures p2 repository. Adds support for bndtools (#11).

## License
The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).

Included binaries from [Jersey](http://jersey.java.net/), version 2.3.1, which are published under two licenses, the [CDDL 1.1 and GPL 2 with CPE](http://glassfish.java.net/public/CDDL+GPL_1_1.html)
