package de.qaware.ssh.shop.service.product;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ProductProviderTest {
    
    @Inject
    ProductProvider provider;
    
    @Test
    void getNoProducts() {
        assertThat(provider.getProducts(Collections.emptySet())).isEmpty();
    }
    
    @Test
    void getSingleProduct() {
        assertThat(provider.getProducts(Set.of(1))).singleElement()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Gaming Laptop Pro")
                .hasFieldOrPropertyWithValue("price", 2499.0);
    }
    
    @Test
    void getMultipleProducts() {
        assertThat(provider.getProducts(Set.of(1, 2, 3))).hasSize(3)
                .extracting("id").containsExactly(1, 2, 3);
    }
    
    @Test
    void getNonExistingProduct() {
        assertThat(provider.getProducts(Set.of(99))).isEmpty();
    }
    
}