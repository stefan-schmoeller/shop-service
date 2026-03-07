package de.qaware.ssh.shop.service;


import de.qaware.ssh.shop.service.product.Product;
import de.qaware.ssh.shop.service.testresources.WithSolrTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

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
        Assertions.assertThat(serializedResponse)
                .hasSize(4)
                .extracting("id").containsExactly(4, 25, 44, 49);
    }
    
}
