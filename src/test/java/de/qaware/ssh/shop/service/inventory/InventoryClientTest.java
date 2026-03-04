package de.qaware.ssh.shop.service.inventory;

import de.qaware.ssh.shop.service.testresources.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class InventoryClientTest {
    
    @Inject
    @RestClient
    InventoryClient client;
    
    @Test
    void getStocks() {
        List<InventoryEntry> stocks = client.getStock(List.of(1, 3, 4));
        assertThat(stocks).hasSize(3).containsExactlyInAnyOrder(
                new InventoryEntry(1, 10), new InventoryEntry(3, 7), new InventoryEntry(4, 0)
        );
    }
    
    @Test
    void getStocksTimeout() {
        List<InventoryEntry> stocks = client.getStock(List.of(1, Integer.MAX_VALUE));
        assertThat(stocks).hasSize(2).containsExactlyInAnyOrder(
                new InventoryEntry(1, Integer.MIN_VALUE), new InventoryEntry(Integer.MAX_VALUE, Integer.MIN_VALUE)
        );
    }
    
    @Test
    void getStocksBadRequest() {
        List<InventoryEntry> stocks = client.getStock(List.of(1, 99));
        assertThat(stocks).hasSize(2).containsExactlyInAnyOrder(
                new InventoryEntry(1, Integer.MIN_VALUE), new InventoryEntry(99, Integer.MIN_VALUE)
        );
    }
    
    @Test
    void getExistingStock() {
        InventoryEntry stock = client.getStock(1);
        assertThat(stock.stock()).isEqualTo(10);
    }
    
    @Test
    void getSingleStockTimeout() {
        InventoryEntry stock = client.getStock(Integer.MAX_VALUE);
        assertThat(stock.stock()).isEqualTo(Integer.MIN_VALUE);
    }
    
    @Test
    void getSingleStockBadRequest() {
        InventoryEntry stock = client.getStock(99);
        assertThat(stock.stock()).isEqualTo(Integer.MIN_VALUE);
    }
    
}