package de.qaware.ssh.shop.service.order;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
class OrderProviderTest {
    
    @Inject
    OrderProvider provider;
    
    @Test
    void combinedTest() {
        UUID orderId = UUID.randomUUID();
        Order orderToStore = new Order(orderId, 1, "receipts/order-" + orderId + ".txt");
        provider.persist(orderToStore);
        
        Order orderFromDb = provider.getOrder(orderId);
        Assertions.assertThat(orderFromDb).isEqualTo(orderToStore);
    }
    
}