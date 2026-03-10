package de.qaware.ssh.shop.service.inventory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class InventoryProvider {

    @Inject
    @RestClient
    InventoryClient inventoryClient;

    public Map<Integer, Integer> getStocks(List<Integer> productIds) {
        return inventoryClient.getStock(productIds)
                .stream()
                .collect(Collectors.toMap(InventoryEntry::id, InventoryEntry::stock));
    }

    public int getStock(int productId) {
        return inventoryClient.getStock(productId).stock();
    }

}
