package com.eclipsesource.jaxrs.karaf.itest;

import com.eclipsesource.jaxrs.sample.model.SimpleMessage;
import com.eclipsesource.jaxrs.sample.service.GreetingResource;
import org.apache.karaf.features.FeaturesService;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;

import javax.inject.Inject;
import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class FeatureIntegrationTest {

    @Inject
    FeaturesService featuresService;

    @Inject
    @Filter(value = "(com.eclipsesource.jaxrs.publish=false)")
    GreetingResource jaxrsGreetingResource;

    @Inject
    @Filter(value = "!(com.eclipsesource.jaxrs.publish=false)")
    GreetingResource greetingResource;

    @Configuration
    public Option[] config() {

        MavenArtifactUrlReference karafUrl = maven()
                .groupId("org.apache.karaf")
                .artifactId("apache-karaf")
                .type("tar.gz")
                .versionAsInProject();

        MavenUrlReference karafStandardRepo = maven().groupId("org.apache.karaf.features")
                .artifactId("standard")
                .classifier("features")
                .type("xml")
                .versionAsInProject();

        MavenUrlReference projectFeatures = maven().groupId("com.eclipsesource.jaxrs")
                .artifactId("features")
                .classifier("features")
                .type("xml")
                .versionAsInProject();

        return new Option[]{
                karafDistributionConfiguration().frameworkUrl(karafUrl)
                        .unpackDirectory(new File("target/exam"))
                        .useDeployFolder(false),
                keepRuntimeFolder(),
                KarafDistributionOption.features(karafStandardRepo, "http", "scr"),
                KarafDistributionOption.features(projectFeatures, "jax-rs-connector", "jax-rs-provider-moxy"),
                mavenBundle().groupId("com.eclipsesource.jaxrs").artifactId("jax-rs-sample").versionAsInProject()
        };
    }

    @Test
    public void testJaxRsConnectorFeatureIsInstalled() throws Exception {
        assertThat(featuresService.isInstalled(featuresService.getFeature("jax-rs-connector")), is(true));
        assertThat(featuresService.isInstalled(featuresService.getFeature("jax-rs-provider-moxy")), is(true));
    }

    @Test
    public void testJaxRsServiceIsPublished() throws Exception {
        assertThat(greetingResource, notNullValue());
        assertThat(jaxrsGreetingResource, notNullValue());
    }

    @Test
    public void testJaxRsClientCallReturnsAGreeting() throws Exception {
        // you can verify that it calls the HTTP service if you change the base url used to publish this consumer
        // see class  ro.ieugen.jaxrs.sample.consumer.PublishConsumerService
        SimpleMessage message = jaxrsGreetingResource.greeting();
        assertThat(message.getMessage(), StringContains.containsString("Hello this is current time"));

    }
}
