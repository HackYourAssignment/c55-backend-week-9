package net.hackyourfuture.hyfshop.product;

import lombok.AllArgsConstructor;
import net.hackyourfuture.hyfshop.product.dto.ProductResponse;
import net.hackyourfuture.hyfshop.product.dto.SetSizeRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam(value = "color", required = false) String color) {
        if (color == null || color.isBlank()) {
            return productService.getAllProducts();
        }

        return productService.searchProducts(color);
    }

    @PutMapping("/{id}/size")
    public ProductResponse setSize(@PathVariable int id, @RequestBody SetSizeRequest request) {
        return productService.setProductSize(id, request.size());
    }

    @PutMapping("/{id}/image")
    public ProductResponse setProductImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        return productService.setProductImage(id, file);
    }

    @DeleteMapping("/{id}/image")
    public ProductResponse deleteProductImage(@PathVariable int id) {
        return productService.deleteProductImage(id);
    }
}

