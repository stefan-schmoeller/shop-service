package de.qaware.ssh.shop.service.order;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(orderFromDb).isEqualTo(orderToStore);
    }

}
