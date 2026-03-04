package de.qaware.ssh.shop.service.product;

public record Product(
        int id,
        String name,
        String description,
        double price
) {
}
