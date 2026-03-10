package de.qaware.ssh.shop.service;

import de.qaware.ssh.shop.service.order.Order;
import de.qaware.ssh.shop.service.product.Product;
import de.qaware.ssh.shop.service.testresources.WithSolrTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@QuarkusIntegrationTest
@WithWireMockTestResource(clients = {"inventory-service"}, logConsumeEnabled = true)
@WithSolrTestResource(collections = {"products"}, logConsumeEnabled = true)
public class ShopIT {

    @Test
    void searchProducts() {
        Response response = given()
                .queryParam("searchTerm", "Display")
                .accept(ContentType.JSON)
                .get("/shop/search");
        response.then().statusCode(200);
        List<Product> serializedResponse = response.as(new TypeRef<>() {});
        assertThat(serializedResponse)
                .hasSize(4)
                .extracting("id").containsExactly(4, 25, 44, 49);
    }

    @Test
    void placeOrderAndGetReceipt() {
        Response orderResponse = given()
                .pathParam("productId", 4)
                .get("/shop/order/{productId}");
        orderResponse.then().statusCode(201);
        Order serializedResponse = orderResponse.as(Order.class);
        assertThat(serializedResponse.productId()).isEqualTo(4);
        UUID orderId = serializedResponse.orderId();
        Response receiptResponse = given()
                .pathParam("orderId", orderId)
                .get("shop/receipt/{orderId}");
        receiptResponse.then()
                .statusCode(200)
                .contentType(ContentType.BINARY)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"receipt.txt\"")
                .body(equalTo("Receipt for Order: " + orderId));
    }

    @Test
    void tryToOrderUnavailableProduct() {
        Response response = given()
                .pathParam("productId", 21)
                .get("/shop/order/{productId}");
        response.then().statusCode(409);
    }

}
