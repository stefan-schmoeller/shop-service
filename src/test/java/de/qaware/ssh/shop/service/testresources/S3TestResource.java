package de.qaware.ssh.shop.service.testresources;

import io.quarkus.test.Mock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Map;

public class S3TestResource implements QuarkusTestResourceLifecycleManager {
    
    private static String testDataHome = "src/test/resources/s3/data"; 

    @Override
    public void init(Map<String, String> initArgs) {
        testDataHome = initArgs.getOrDefault("testDataHome", "src/test/resources/s3/data");
    }

    @Override
    public Map<String, String> start() {
        return Map.of("receipt.bucket.name", "receipt-test-bucket");
    }

    @Override
    public void stop() {
        // We only access the local file system so there is nothing to stop here
    }

    @Mock
    public S3Client produceS3Client() {
        return new S3TestClient(testDataHome);
    }

}
