package de.qaware.ssh.shop.service;

import de.qaware.ssh.shop.service.inventory.InventoryProvider;
import de.qaware.ssh.shop.service.order.Order;
import de.qaware.ssh.shop.service.order.OrderProvider;
import de.qaware.ssh.shop.service.product.Product;
import de.qaware.ssh.shop.service.product.ProductProvider;
import de.qaware.ssh.shop.service.receipt.ReceiptProvider;
import de.qaware.ssh.shop.service.search.SearchProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
@Path("shop")
@Produces(MediaType.APPLICATION_JSON)
public class ShopResource {

    @Inject
    public ShopResource(SearchProvider searchProvider, ProductProvider productProvider, OrderProvider orderProvider, InventoryProvider inventoryProvider, ReceiptProvider receiptProvider) {
        this.searchProvider = searchProvider;
        this.productProvider = productProvider;
        this.orderProvider = orderProvider;
        this.inventoryProvider = inventoryProvider;
        this.receiptProvider = receiptProvider;
    }

    @Inject
    SearchProvider searchProvider;
    @Inject
    ProductProvider productProvider;
    @Inject
    OrderProvider orderProvider;
    @Inject
    InventoryProvider inventoryProvider;
    @Inject
    ReceiptProvider receiptProvider;

    @GET
    @Path("/search")
    public Response search(@QueryParam("searchTerm") String searchTerm) {
        // Search IDs of products matching the search term
        List<Integer> ids = searchProvider.query(searchTerm);

        // Check availability
        Map<Integer, Integer> stocks = inventoryProvider.getStocks(ids);
        stocks.entrySet().removeIf(entry -> entry.getValue() <= 0);

        // Resolve available products
        List<Product> products = productProvider.getProducts(stocks.keySet());

        return Response.ok(products).build();
    }

    @GET
    @Path("/order/{productId}")
    @Transactional
    public Response placeOrder(@PathParam("productId") Integer productId) {
        // Check if product is available
        if (inventoryProvider.getStock(productId) <= 0) {
            return Response.status(Response.Status.CONFLICT).entity("Currently sold out").build();
        }

        UUID orderId = UUID.randomUUID();

        // Generate S3 key
        String s3Key = "receipts/order-" + orderId + ".txt";

        // Persist order
        Order order = new Order(orderId, productId, s3Key);
        orderProvider.persist(order);

        // Persist receipt in S3
        receiptProvider.persist(s3Key, orderId);

        // Return order details
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    @Path("/receipt/{orderId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("orderId") UUID orderId) {
        // Identify related order
        Order order = orderProvider.getOrder(orderId);

        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Get receipt from S3 bucket
        byte[] data = receiptProvider.getReceipt(order.receiptKey());

        // Create binary response
        return Response.ok(data)
                .header("Content-Disposition", "attachment; filename=\"receipt.txt\"")
                .build();
    }

}
