package net.hackyourfuture.hyfshop.product;

import jakarta.annotation.Nullable;
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

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }

    @GetMapping(params = "color")
    public List<ProductResponse> searchProducts(@Nullable @RequestParam String color) {
        if (color == null) {
            return productService.getAllProducts();
        }
        return productService.searchProducts(color);
    }

    @PutMapping("/{id}/size")
    public ProductResponse setProductSize(@PathVariable int id, @RequestBody SetSizeRequest request) {
        return productService.setProductSize(id, request.size());
    }

    @PutMapping("/{id}/image")
    public ProductResponse setProductImage(@PathVariable int id, @RequestBody MultipartFile file) {
        return productService.setProductImage(id, file);
    }

    @DeleteMapping("/{id}/image")
    public ProductResponse deleteProductImage(@PathVariable int id) {
        return productService.deleteProductImage(id);
    }
}

