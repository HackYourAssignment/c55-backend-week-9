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
        String imageUrl = fileService.upload(file); // Uploading the file and getting back the public URL string
        productRepository.setImageUrl(id, imageUrl); // Save the URL to the database image_url column
        return ProductResponse.from(productRepository.findById(id));
    }

    public ProductResponse deleteProductImage(int id) {
        Product product = productRepository.findById(id);
        // check if an image exists
        if (product != null && product.getImageUrl() != null) {
            fileService.delete(product.getImageUrl());
        }
        productRepository.setImageUrl(id, null); // Update the database image_url column to null
        return ProductResponse.from(productRepository.findById(id));
    }
}
