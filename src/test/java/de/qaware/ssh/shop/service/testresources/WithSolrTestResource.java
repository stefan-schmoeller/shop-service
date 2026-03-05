package de.qaware.ssh.shop.service.testresources;

import io.quarkus.test.common.QuarkusTestResource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a QuarkusIntegrationTest test to simulate a solr cloud instance
 */
@QuarkusTestResource(SolrTestResource.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithSolrTestResource {

    /**
     * Version of the Solr Cloud image to use.
     *
     * @return the solr version
     */
    String version() default "9.10.1";

    /**
     * The names of the collections that shall be created.
     *
     * @return array of collection names
     */
    String[] collections();

    /**
     * Set to true if Solr logs should be included in test logs
     *
     * @return true if logging shall be enabled
     */
    boolean logConsumeEnabled() default false;

}
