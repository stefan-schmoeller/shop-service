package de.qaware.ssh.shop.service.order;

import java.util.UUID;

public record Order(
        UUID orderId,
        int productId,
        String receiptKey
) {
}
