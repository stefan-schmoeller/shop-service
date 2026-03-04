package de.qaware.ssh.shop.service.inventory;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.util.List;

@RegisterRestClient(configKey = "inventory-service", baseUri = "http://inventory-service.inventory.svc.cluster.local:8080")
@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface InventoryClient {
    
    @GET
    @Timeout(500)
    @Fallback(fallbackMethod = "stockFallback", applyOn = {TimeoutException.class, ClientWebApplicationException.class})
    List<InventoryEntry> getStock(@QueryParam("id") List<Integer> productIds);
    
    @GET
    @Path("/{id}")
    @Timeout(500)
    @Fallback(fallbackMethod = "stockFallback", applyOn = {TimeoutException.class, ClientWebApplicationException.class})
    InventoryEntry getStock(@PathParam("id") Integer productId);
    
    default List<InventoryEntry> stockFallback(List<Integer> productIds) {
        return productIds.stream().map(id -> new InventoryEntry(id, Integer.MIN_VALUE)).toList();
    }
    
    default InventoryEntry stockFallback(Integer productId) {
        return new InventoryEntry(productId, Integer.MIN_VALUE);
    }
    
}
