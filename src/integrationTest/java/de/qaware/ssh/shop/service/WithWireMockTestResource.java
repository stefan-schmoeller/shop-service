package de.qaware.ssh.shop.service;

import io.quarkus.test.common.QuarkusTestResource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a QuarkusIntegrationTest test to mock backend calls
 */
@QuarkusTestResource(WireMockTestResource.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithWireMockTestResource {

    /**
     * Version of the WireMock image to use.
     *
     * @return the Wiremock version
     */
    String version() default "3.13.2";

    /**
     * The names(=configKey) of the backends that shall be mocked.
     *
     * @return array of backend configKeys
     */
    String[] clients();

    /**
     * Set to true if WireMock logs should be included in test logs
     *
     * @return true if logging shall be enabled
     */
    boolean logConsumeEnabled() default false;

    /**
     * Set to true if WireMock logging should be verbose
     *
     * @return true if verbose logging shall be enabled
     */
    boolean verbose() default false;

}
