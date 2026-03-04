package de.qaware.ssh.shop.service.testresources;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class S3TestClient implements S3Client {
    
    private final Path dataPath;
    
    public S3TestClient(String path) {
        dataPath = Path.of(path);
    }
    
    @Override
    public String serviceName() {
        return SERVICE_NAME;
    }
    
    @Override
    public void close() {
        // We only access the local file system so there is nothing to close here
    }
    
    @Override
    public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody) throws AwsServiceException, SdkClientException {
        // Create data directory for bucket if it does not exist already
        Path bucketPath = dataPath.resolve(putObjectRequest.bucket());
        Path documentFile = bucketPath.resolve(putObjectRequest.key());
        documentFile.toFile().getParentFile().mkdirs();
        try {
            Path docFile = Files.createFile(bucketPath.resolve(putObjectRequest.key()));
            docFile.toFile().deleteOnExit();
            FileWriter writer = new FileWriter(docFile.toFile());
            writer.write(new String(requestBody.contentStreamProvider().newStream().readAllBytes()));
            writer.flush();
            return PutObjectResponse.builder().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
    }
    
    @Override
    public ResponseBytes<GetObjectResponse> getObjectAsBytes(GetObjectRequest getObjectRequest) throws AwsServiceException, SdkClientException {
        Path documentFile = dataPath.resolve(getObjectRequest.bucket()).resolve(getObjectRequest.key());
        try {
            GetObjectResponse response = GetObjectResponse.builder()
                    .contentLength(Files.size(documentFile))
                    .contentType(Files.probeContentType(documentFile))
                    .build();
            return ResponseBytes.fromByteArray(response, Files.readAllBytes(documentFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
