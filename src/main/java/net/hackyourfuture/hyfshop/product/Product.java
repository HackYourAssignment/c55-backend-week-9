package net.hackyourfuture.hyfshop.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {
    private int id;
    private String title;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private Map<String, Object> details;
}
