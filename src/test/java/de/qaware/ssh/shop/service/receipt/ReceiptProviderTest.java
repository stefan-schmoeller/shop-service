package de.qaware.ssh.shop.service.receipt;

import de.qaware.ssh.shop.service.testresources.S3TestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(S3TestResource.class)
class ReceiptProviderTest {

    @Inject
    ReceiptProvider receiptProvider;

    @Test
    void combinedTest() {
        UUID orderId = UUID.randomUUID();
        String s3Key = "receipts/order-1234.txt";
        receiptProvider.persist(s3Key, orderId);
        byte[] receiptContent = receiptProvider.getReceipt(s3Key);
        assertThat(receiptContent).asString().isEqualTo("Receipt for Order: " + orderId);
    }

}
