package dev.sorokin.productservice.api;

import java.math.BigDecimal;

public record ProductCreateRequest(
        String name,
        BigDecimal price,
        String description
) {
}
