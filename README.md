OSGi - JAX-RS Connector 2.4.0
=============================

[JAX-RS (JSR 311)](http://jsr311.java.net/) is the community-driven Standard for 
building RESTful web services with Java. The reference implementation for JAX-RS is 
[Jersey](http://jersey.java.net/) and ships as OSGi bundles. This project connects 
Jersey and OSGi at the service level. This means that OSGi services can be published as 
RESTful web services by simply registering them as OSGi services and also consumed as OSGi services ;).

Features
--------

When the `com.eclipsesource.jaxrs.connector` bundle is activated all `@Path`/`@Provider` annotated
objects that are registered as OSGi services will be published as RESTful web services.
Services can be added or removed during runtime and will be published or postponed.
To publish services on different ports the `http.port` service property can be used.
The connector detects configuration changes during runtime (e.g. when done by the 
OSGi Configuration Admin Service).  

The second major feature of the connector is the consumer part. The idea behind this is to be able to reuse the @Path/@Provider 
interfaces for consuming the services. See these posts for a detailed description: [Consuming REST services in Java the cool way](http://eclipsesource.com/blogs/2012/11/27/consuming-rest-services-in-java-the-cool-way/), [Consuming REST services in OSGi the cool way](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/).

Installation
------------

Install from this software repository into your target: http://hstaudacher.github.com/osgi-jax-rs-connector 
or download the binaries:

* [com.eclipsesource.jaxrs.connector_2.4.0.201302282147.jar](http://hstaudacher.github.com/osgi-jax-rs-connector/plugins/com.eclipsesource.jaxrs.connector_2.4.0.201302282147.jar)
* [com.eclipsesource.jaxrs.consumer_1.2.0.201302282147.jar](http://hstaudacher.github.com/osgi-jax-rs-connector/plugins/com.eclipsesource.jaxrs.consumer_1.2.0.201302282147.jar)

If dependencies can't be satisfied please disable "include required software" within the target editor.

Usage
-----

* For Eclipse: Add the com.eclipsesource.jaxrs.connector.feature (OSGi JAX-RS Connector) to your target using the url above.
* Add the com.eclipsesource.jaxrs.connector and the 3 Jersey bundles to your OSGi instance.
* Convert some OSGi service to resources like in [this tutorial](http://jersey.java.net/nonav/documentation/latest/getting-started.html#d4e45)
* Point your client to the specified url. Don't forget that the default root path is `/services`. So registering a 
service with the path `/example` would lead to `/services/example`.  
* [Read this post to see how to handle the consumer.](http://eclipsesource.com/blogs/2012/11/28/consuming-rest-services-in-osgi-the-cool-way/)

Examples
--------
This git repository contains two example bundles. A @Path annotated resource will be registred in `com.eclipsesource.jaxrs.connector.example` 
and `com.eclipsesource.jaxrs.connector.example.ds`. In the `ds` bundle OSGi Declarative Services will be used to register
the service and it provides an additional example for @Provider annotated providers. If you want to run the examples within
your Eclipse IDE use the launch configurations located in the bundles.  
After you have started the bundles a service will be available at `http://localhost:9090/services/osgi-jax-rs`.

Requirements
------------

* OSGi Core Specification 4.2 and an OSGi HttpService implementation (e.g. Equinox, Felix).
* JRE 1.6 (same as Jersey)

Alternatives
------------

* [Neil Bartlett's jaxrs-osgi-extender](https://github.com/njbartlett/jaxrs-osgi-extender)
* OSGi Remote Services (e.g. [Apache CXF](http://cxf.apache.org/distributed-osgi-reference.html#DistributedOSGiReference-ServiceProviderpropertiesForConfiguringRESTfulJAXRSbasedendpointsandconsumers))

Jersey version
--------------

Jersey 2.0 (m12) is included in the software repository.  

Changelog
---------

* **1.0 (Jan 23rd, 2012):** Functionality to publish `@Path` annotated OSGi services as web resources.
* **2.0 (Aug 27th, 2012):** Use the config admin to specify the root path. The default root path is /services. You can configure the path by specifying a config with the service.pid `com.eclipsesource.jaxrs.connector` and the property `root`. The path needs to be a valid servlet path e.g. "/api".  
* **2.1 (Oct 26th, 2012):** Besides `@Path` annotated object it's now possible to register `@Provider` annotated objects as OSGi services too. Thanks to Dirk Lecluse for this contribution.
* **2.1.1 (Nov 11th, 2012):** Updated included Jersey Version (1.15).
* **2.2.0 (Jan 3rd, 2013):** Updated included Jersey Version (2.0 m11). Added Consumer integration.
* **2.3.0 (Jan 22th, 2013):** Added service property to exclude a resource from publishing. See `ServiceProperties`.
* **2.4.0 (Feb 28th, 2013):** Updated included Jersey Version (2.0 m12).

License
-------

The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).

Included binaries from [Jersey](http://jersey.java.net/), version 2.0 m12, which are published under two licenses, the [CDDL 1.1 and GPL 2 with CPE](http://glassfish.java.net/public/CDDL+GPL_1_1.html)
