package net.hackyourfuture.hyfshop.product;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.hyfshop.file.FileService;
import net.hackyourfuture.hyfshop.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    public List<ProductResponse> getAllProducts() {
        return productRepository.getAllProducts().stream().map(ProductResponse::from).toList();
    }

    public List<ProductResponse> searchProducts(String color) {
        return productRepository.findByColor(color).stream().map(ProductResponse::from).toList();
    }

    public ProductResponse setProductSize(int id, String size) {
        productRepository.setSize(id, size);
        return ProductResponse.from(productRepository.findById(id));
    }

    public ProductResponse setProductImage(int id, MultipartFile file) {
        try {
            Product product = productRepository.findById(id);
            if (product.getImageUrl() != null) {
                fileService.deleteByUrl(product.getImageUrl());
            }
            String imageUrl = fileService.upload(file);
            productRepository.setImageUrl(id, imageUrl);

            Product updatedProduct = productRepository.findById(id);
            return ProductResponse.from(updatedProduct);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload product image", e);
        }
    }

    public ProductResponse deleteProductImage(int id) {
        Product product = productRepository.findById(id);

        if (product.getImageUrl() != null) {
            fileService.deleteByUrl(product.getImageUrl());
        }

        productRepository.setImageUrl(id, null);

        Product updatedProduct = productRepository.findById(id);
        return ProductResponse.from(updatedProduct);
    }
}
