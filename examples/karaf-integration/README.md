# Karaf feature for osgi-jax-rs-connector
You can find the sources for the osgi-jax-rs-connector here [https://github.com/hstaudacher/osgi-jax-rs-connector/](https://github.com/hstaudacher/osgi-jax-rs-connector/).

This is a 'packaging' of the bundles for easy deployment inside Apache Karaf.

## Building and running
Building the project requires Apache Maven:
```
mvn clean install
```
should do the trick.

You will get some files installed in your local Maven repo:

* a **features.xml** a list of bundles for Karaf to install
* **jax-rs-sample** - a sample jax-rs project used for demo

To make use of the project download the latest [http://karaf.apache.org/](Apache Karaf). Follow the installation instructions (unpack and run `./bin/karaf` ).

In the Karaf shell run the following commands:
```
feature:repo-add mvn:com.eclipsesource.jaxrs/features/0.0.1-SNAPSHOT/xml/features
feature:install scr http
feature:install jax-rs-connector jax-rs-provider-moxy
install mvn:com.eclipsesource.jaxrs/jax-rs-sample/0.0.1-SNAPSHOT
```

The first line adds a features repository to let Karaf know how to install the connector.

Line two installs the `scr` (Declarative Services ) and `http` (Http Service) features. Our sample bundle exports a service via SCR and we need `http` to make jax-rs connector work.

Third line installs the jax-rs connector and a provider for JSON marshalling (used in our sample).

The last line installs our sample application that exposes a JAX-RS annotated service via Declarative Serivces.

Once the application is deployed, visit  [http://localhost:8181/services/greeting](http://localhost:8181/services/greeting)
