# OSGi - JAX-RS Connector 5.3.1
[![Build Status](https://travis-ci.org/hstaudacher/osgi-jax-rs-connector.png)](https://travis-ci.org/hstaudacher/osgi-jax-rs-connector) [![Maven Status](https://maven-badges.herokuapp.com/maven-central/com.eclipsesource.jaxrs/publisher/badge.png)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.eclipsesource.jaxrs%22) [![License](http://img.shields.io/badge/license-EPL-blue.svg)](http://www.eclipse.org/legal/epl-v10.html) [![Gitter](http://img.shields.io/badge/Gitter-join-yellow.svg)](https://gitter.im/hstaudacher/osgi-jax-rs-connector?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

![](http://download.eclipsesource.com/~hstaudacher/connector.png)

[JAX-RS (JSR 311)](http://jsr311.java.net/) is the community-driven standard for
building RESTful web services with Java. The reference implementation for JAX-RS is
[Jersey](http://jersey.java.net/) and ships as OSGi bundles.
This project connects Jersey and OSGi at the *service level*. This means that OSGi services can be published as
RESTful web services by simply registering them as OSGi services. A neat side feature is that REST services can also be consumed as OSGi services ;).  

*To see how to get started with JAX-RS 2.0 and Jersey please read the [Jersey getting started guide](https://jersey.java.net/documentation/latest/getting-started.html).*
mox
## Features
The OSGi-JAX-RS Connector provides **two main bundles**. A **publisher** and a **consumer**. Both can be used completely separately or together, it's up to you. Additional the connector provides custom `@Provider` and `Feature` implementations that can be used optionally.

### Publisher
The publisher is located in the bundle `com.eclipsesource.jaxrs.publisher`. All it does is tracking OSGi services. When it spots a service that is annotated with the JAX-RS annotations `@Path`/`@Provider` or it extends  `Feature` the work begins. The publisher hooks these services into Jersey and the OSGi HTTPService. Basically this means it publishes them as RESTful web services. It's just that simple!

By default the publisher registers the services using the context path `/services`. This means an OSGi service that is annotated with `@Path( "/foo" )` will be available using the path `/services/foo`. This context path is configurable using the OSGi configuration admin. You can configure the service using the service.pid `com.eclipsesource.jaxrs.connector` with the following properties:

* `root` : defines a custom root path. Default is `/services`.
* `publishDelay` : the time in ms to wait after a resource was registered before its going to be published. Default is `150`.

Besides the config admin you can [configure the JAX-RS Application with properties](https://jersey.java.net/documentation/latest/appendix-properties.html) too. Simply register a service implementing the [ApplicationConfiguration](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/bundles/com.eclipsesource.jaxrs.publisher/src/com/eclipsesource/jaxrs/publisher/ApplicationConfiguration.java) interface. It will be called before the Application got published.

As said earlier, the publisher uses the OSGi HTTPService to publish the services. As a result all configuration topics regarding ports, protocol and so on are up to the HTTPService implementation of your choice.

If it's your wish to publish services on different ports, just register them and add the service property `http.port` with the port of your choice. Of course it's necessary that an HTTPService is up and running on such a port.

### Consumer
The consumer is located in the bundle `com.eclipsesource.jaxrs.consumer`. The idea of the consumer is to reuse your `@Path` and `@Provider` interfaces for calling a service. From a technical point of view it takes your interfaces together with a base url and creates Java Proxies. These proxies will make an HTTP call when a method will be invoked on it. The proxy knows which http method, parameters and so on it should use because you have it defined with the JAX-RS annotations. The consumer uses the JAX-RS 2.0 client API to send requests. So, there will be no additional dependencies.

A nice side effect of the consumer is, that it does not need OSGi. It's just a jar that can be used to create the mentioned proxies. See the [ConsumerFactory](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/bundles/com.eclipsesource.jaxrs.consumer/src/com/eclipsesource/jaxrs/consumer/ConsumerFactory.java) for more information.

When using it together with OSGi it provides a helper to create your proxies and automatically register them as OSGi services. But it's up to you if you want to take care regarding the publishing by your own. See the [ConsumerPublisher](https://github.com/hstaudacher/osgi-jax-rs-connector/blob/master/bundles/com.eclipsesource.jaxrs.consumer/src/com/eclipsesource/jaxrs/consumer/ConsumerPublisher.java) for more information.

A detailed explanation of the concepts of the consumer together with some examples can be found on our blog: [Consuming REST services in Java the cool way](http://eclipsesource.com/blogs/2012/11/27/consuming-rest-services-in-java-the-cool-way/), [Consuming REST services in OSGi the cool way](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/).

### Providers
The custom `@Provider` and `Feature` implementations are located in their own features. The following features are currently included.
* `com.eclipsesource.jaxrs.provider.moxy` - Allows the de/serialization using [EclipseLink MOXy](https://www.eclipse.org/eclipselink/#moxy).  
* `com.eclipsesource.jaxrs.provider.gson` - Allows the de/serialization using [Gson](https://code.google.com/p/google-gson/).  
* `com.eclipsesource.jaxrs.provider.security` - Provides an OSGi friendly integration of Jersey's/JAX-RS security features. [Read the wiki for more information](https://github.com/hstaudacher/osgi-jax-rs-connector/wiki/security).
* `com.eclipsesource.jaxrs.provider.sse` - Provides an integration of Jersey's [SSE feature](https://jersey.java.net/documentation/latest/sse.html) *(requires javax.servlet 3.x)*.
* `com.eclipsesource.jaxrs.provider.multipart` - Provides an integration of Jersey's [Multipart feature](https://jersey.java.net/documentation/latest/media.html#multipart).
* `com.eclipsesource.jaxrs.provider.swagger` - Provides an integration of [Swagger](http://swagger.io/). See the [swagger wiki page](https://github.com/hstaudacher/osgi-jax-rs-connector/wiki/Swagger-Integration) for more details.


## Installation
To ease the installation we provide a p2 repository and we publish the connector to maven central.

### p2
Install from this software repository into your target: `http://hstaudacher.github.io/osgi-jax-rs-connector`

**Please note:** If dependencies can't be satisfied please disable "include required software" within the target editor.

### Maven
All artifacts of this projects are available in Maven Central with the group id [`com.eclipsesource.jaxrs`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.eclipsesource.jaxrs%22).

## Usage
> A detailed step-by-step can be found [here](http://eclipsesource.com/blogs/2014/02/04/step-by-step-how-to-bring-jax-rs-and-osgi-together/).

Basically all you need to to is add the publisher and/or the consumer to your OSGi container and start them. For people new to OSGi the steps to get started with the connector and Eclipse are described below.

1. Add the com.eclipsesource.jaxrs.connector.feature (OSGi JAX-RS Connector) to your target using the url mentioned in the Installation/p2 section.
2. Add the `com.eclipsesource.jaxrs.publisher` and its dependencies bundles to your OSGi instance.
3. Convert some OSGi services to resources like in [this tutorial](http://jersey.java.net/nonav/documentation/latest/getting-started.html#new-project-structure)
4. Point your client to the specified url. Don't forget that the default root path is `/services`. So registering a
service with the path `/example` would lead to `/services/example`.

The steps how the consumer will be handled are dscribed in this [post](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/).

## Examples
Examples for the publisher and consumer can be found within the [examples](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples) folder.

> When using the example's Eclipse launch configuration please ensure that you have selected the connector bundles from your target.

### Publisher Examples
Some examples exist for the publisher. They are located in the bundles `com.eclipsesource.jaxrs.connector.example` and `com.eclipsesource.jaxrs.connector.example.ds`. As the names say one uses Declarative services and the other don't.
In both exampels a simple POJO is annotated with `@Path` and will be registered as an OSGIi service. Both bundles contain an Eclipse Launch Configuration to start them (please don't forget to add the publisher bundle to the launch config). After starting the launch configs the service will be available at `http://localhost:9090/services/osgi-jax-rs`.

As a bonus the `com.eclipsesource.jaxrs.connector.example.ds` contains an example how to use the `@Provider` together with the publisher.

Besides these basic example two example exist that are shwoing how the [security integration](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples/com.eclipsesource.jaxrs.security.example) and [Jersey's SSE integration](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples/com.eclipsesource.jaxrs.sse.example) works.

### Consumer Examples
The example for the consumer is splitted into two bundles called `com.eclipsesource.jaxrs.consumer.example` and `com.eclipsesource.jaxrs.consumer.example.caller`. The example bundle contains the data model and the resource interface while the caller just calls the service. The example fetches data from github and is pretty simple.

### Apache Karaf Integration
If you want to deploy the connector into [Apache Karaf](http://karaf.apache.org/) take a look at the [karaf-integration example](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples/karaf-integration). To get started read the step-by-step guide in the [README](https://github.com/hstaudacher/osgi-jax-rs-connector/tree/master/examples/karaf-integration/README.md).

### Starter Kit
If you are working with [Bndtools](http://bndtools.org), a [Starter Kit](https://github.com/BryanHunt/bndtools-equinox-app-kit) is available to help you get going.  You will most likely want either the [rest](https://github.com/BryanHunt/bndtools-equinox-app-kit/tree/rest) or [mongo-rest](https://github.com/BryanHunt/bndtools-equinox-app-kit/tree/mongo-rest) branch.

## Requirements
* OSGi Core Specification R4 and an OSGi HttpService implementation (e.g. Equinox, Felix).
* JRE 1.7 (same as Jersey 2.6+)
* *If you want to use the SSE provider you need javax.servlet API 3.x*

## Jersey version
With Jersey 2.0 the library was splitted into several modules with a whole bunch of dependencies. To ease the OSGi application development Jersey was rebundled in this project and ships as a single bundle called `com.eclipsesource.jaxrs.jersey.all`. Currently it includes Jersey 2.22.2 and it's dependencies. Also the Eclipse Source Bundle will be shipped to make the Jersey API more discoverable when using Eclipse. Anyway, it's up to you to use this 'all' bundle or to fetch the single Jersey bundles yourself. The connector just imports packages.

## Changelog
Checkout the [github releases](https://github.com/hstaudacher/osgi-jax-rs-connector/releases).

## License
The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).

Included binaries from [Jersey](http://jersey.java.net/) (rebundled), version 2.22.2, which are published under two licenses, the [CDDL 1.1 and GPL 2 with CPE](http://glassfish.java.net/public/CDDL+GPL_1_1.html).

Included binaries from [Swagger](http://swagger.io/) (rebundled), version 1.5.7, which are published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
