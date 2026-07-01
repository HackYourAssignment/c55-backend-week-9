package net.hackyourfuture.hyfshop.product;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.hyfshop.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.getAllProducts().stream().map(ProductResponse::from).toList();
    }

    public List<ProductResponse> searchProducts(String color) {
        return productRepository.findByColor(color).stream().map(ProductResponse::from).toList();
    }

    public ProductResponse getProductById(int id) {
        return ProductResponse.from(productRepository.findById(id));
    }

    public ProductResponse setProductSize(int id, String size) {
        return ProductResponse.from(productRepository.setSize(id, size));
    }

    public ProductResponse setProductImage(int id, MultipartFile file) {
        // TODO: Implement
        // call ProductRepository.setImageUrl() afterwards with the new URL
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ProductResponse deleteProductImage(int id) {
        // TODO: Implement
        // call ProductRepository.setImageUrl() to set the image url to null
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
