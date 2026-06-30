package net.hackyourfuture.hyfshop.product.dto;

import net.hackyourfuture.hyfshop.product.Product;

import java.math.BigDecimal;
import java.util.Map;

public record ProductResponse (
        int id,
        String title,
        BigDecimal price,
        String category,
        String imageUrl ,
        Map<String, Object> details
){
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getCategory(),
                product.getImageUrl(),
                product.getDetails()
        );
    }
}
