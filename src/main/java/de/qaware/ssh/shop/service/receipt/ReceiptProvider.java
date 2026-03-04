package de.qaware.ssh.shop.service.receipt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@ApplicationScoped
public class ReceiptProvider {
    
    @Inject
    S3Client s3Client;
    
    @Inject
    @ConfigProperty(name = "receipt.bucket.name", defaultValue = "receipt-bucket")
    String receiptBucketName;
    
    public void persist(String s3Key, UUID orderId) {
        RequestBody receiptBody = RequestBody.fromString("Receipt for Order: " + orderId);
        s3Client.putObject(buildPutRequest(s3Key), receiptBody);
    }
    
    public byte[] getReceipt(String s3Key) {
        return s3Client.getObjectAsBytes(buildGetRequest(s3Key)).asByteArray();
    }
    
    private PutObjectRequest buildPutRequest(String s3Key) {
        return PutObjectRequest.builder().bucket(receiptBucketName).key(s3Key).build();
    }
    
    private GetObjectRequest buildGetRequest(String s3Key) {
        return GetObjectRequest.builder().bucket(receiptBucketName).key(s3Key).build();
    }
    
}
