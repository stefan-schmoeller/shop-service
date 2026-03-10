package de.qaware.ssh.shop.service;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class WireMockTestResource implements QuarkusTestResourceConfigurableLifecycleManager<WithWireMockTestResource>, DevServicesContext.ContextAware {

    private GenericContainer<?> wireMockContainer;

    private Optional<String> containerNetworkId;

    // Version of the WireMock Image to use.
    private String wireMockVersion;
    private String[] clients;

    private boolean logConsumeEnabled;
    private boolean verboseLoggingEnabled;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @Override
    public void init(WithWireMockTestResource annotation) {
        wireMockVersion = annotation.version();
        clients = Objects.requireNonNullElse(annotation.clients(), new String[0]);
        logConsumeEnabled = annotation.logConsumeEnabled();
        verboseLoggingEnabled = annotation.verbose();
    }

    @Override
    public Map<String, String> start() {
        wireMockContainer = createContainer("wiremock/wiremock:" + wireMockVersion);
        if (verboseLoggingEnabled) {
            wireMockContainer.withEnv("WIREMOCK_OPTIONS", "--verbose");
        }
        containerNetworkId.ifPresent(wireMockContainer::withNetworkMode);

        wireMockContainer.start();

        Map<String, String> configuration = new HashMap<>();
        String networkHostname = Objects.requireNonNull(wireMockContainer.getCurrentContainerInfo().getConfig().getHostName());
        for (String client : clients) {
            configuration.put("quarkus.rest-client." + client + ".url", "http://" + networkHostname + ":8080");
        }
        return configuration;
    }

    // This is only for testing purposes, so there should be no harm if some resources are not closed
    @SuppressWarnings("java:S2095")
    private GenericContainer<?> createContainer(String wireMockImage) {
        return new GenericContainer<>(DockerImageName.parse(wireMockImage))
                .withExposedPorts(8080)
                .withLogConsumer(consumer(logConsumeEnabled))
                .waitingFor(Wait.forHttp("/__admin/health").forPort(8080))
                .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock", 0777), "/home/wiremock");
    }

    @SuppressWarnings("java:S106") // We want the output in stdout for testing purposes
    private static Consumer<OutputFrame> consumer(boolean enabled) {
        return outputFrame -> {
            if (enabled) {
                System.out.println("[Wiremock] " + outputFrame.getUtf8String().stripTrailing());
            }
        };
    }

    @Override
    public void stop() {
        if (wireMockContainer != null) {
            wireMockContainer.stop();
        }
    }

}
