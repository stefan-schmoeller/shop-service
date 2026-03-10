package de.qaware.ssh.shop.service.testresources;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Resource class to handle {@link WithSolrTestResource} annotation
 */
public class SolrTestResource implements QuarkusTestResourceConfigurableLifecycleManager<WithSolrTestResource>, DevServicesContext.ContextAware {

    private static final String SOLR_CONFIG_SET_PATH = "/opt/solr/server/solr/configsets/schema";
    private static final String SOLR_DATA_PATH = "/var/solr/data";

    private GenericContainer<?> solrContainer;

    // Version of the Solr Docker Image to use.
    private String solrCloudVersion;
    private String[] solrCollections;

    private Optional<String> containerNetworkId;

    boolean logConsumeEnabled;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @Override
    public void init(WithSolrTestResource annotation) {
        solrCloudVersion = annotation.version();
        if (annotation.collections() == null || annotation.collections().length == 0) {
            throw new IllegalStateException("No collections have been specified. Please add the collections property at the WithSolrTestResource annotation");
        }
        solrCollections = annotation.collections();
        logConsumeEnabled = annotation.logConsumeEnabled();
    }

    @Override
    @SuppressWarnings("java:S2142") // Losing information about thread interruption is not critical in test code
    public Map<String, String> start() {
        if (solrCollections == null || solrCollections.length == 0) {
            throw new IllegalStateException("No Solr schema has been specified. Please add an collections property at the WithSolrTestResource annotation");
        }

        solrContainer = createContainer("solr:" + solrCloudVersion);
        containerNetworkId.ifPresent(solrContainer::withNetworkMode);

        solrContainer.start();

        // create collections and config sets
        for (String collection : solrCollections) {
            createCollection(collection);
            populateCollection(collection);
        }

        return defineConfiguration();
    }

    // This is only for testing purposes, so there should be no harm if some resources are not closed
    @SuppressWarnings("java:S2095")
    private GenericContainer<?> createContainer(String solrDockerImage) {
        return new GenericContainer<>(DockerImageName.parse(solrDockerImage))
            .withExposedPorts(8983, 5005)
            .withEnv("SOLR_OPTS", "-Dsolr.disable.shardsWhitelist=true")
            .withLogConsumer(consumer(logConsumeEnabled))
            .waitingFor(Wait.forHttp("/").forPort(8983))
            .withCopyFileToContainer(MountableFile.forClasspathResource("solr/config", 0777), SOLR_CONFIG_SET_PATH)
            .withCopyFileToContainer(MountableFile.forClasspathResource("solr/data", 0777), SOLR_DATA_PATH)
            .withCommand("/opt/solr/docker/scripts/docker-entrypoint.sh solr start -c -f");
    }

    private void createCollection(String collection) {
        try {
            // create the collection -c, with the config set named -n at path -d
            Container.ExecResult result = solrContainer.execInContainer("solr", "create", "-c", collection, "-d", SOLR_CONFIG_SET_PATH + "/" + collection, "-n", collection);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException(String.format(
                        "Creating Solr collection '%s' failed with exit code %d: %s",
                        collection, result.getExitCode(), result.getStderr()
                ));
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(
                    "Failed to set up Solr collection '" + collection + "'. Check container logs for details.", e
            );
        }
    }

    private void populateCollection(String collection) {
        try {
            // populate the collection -c, with the data stored in /var/solr/data/{collection}
            Container.ExecResult result = solrContainer.execInContainer("solr", "post", "-c", collection, "/var/solr/data/" + collection + "/*");
            if (result.getExitCode() != 0) {
                throw new IllegalStateException(String.format(
                        "Creating Solr collection '%s' failed with exit code %d: %s",
                        collection, result.getExitCode(), result.getStderr()
                ));
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(
                    "Failed to set up Solr collection '" + collection + "'. Check container logs for details.", e
            );
        }
    }

    @SuppressWarnings("java:S106") // We want the output in stdout for testing purposes
    private static Consumer<OutputFrame> consumer(boolean enabled) {
        return outputFrame -> {
            if (enabled) {
                System.out.println("[Solr] " + outputFrame.getUtf8String().stripTrailing());
            }
        };
    }

    private Map<String, String> defineConfiguration() {
        if (containerNetworkId.isPresent()) {
            String networkHostname = Objects.requireNonNull(solrContainer.getCurrentContainerInfo().getConfig().getHostName());
            return Map.of(
                    "solr.base.url", "http://" + networkHostname + ":8983/solr"
            );
        } else {
            return Map.of(
                    "solr.base.url", "http://localhost:" + solrContainer.getMappedPort(8983) + "/solr"
            );
        }
    }

    @Override
    public void stop() {
        if (solrContainer != null) {
            solrContainer.stop();
        }
    }

}
