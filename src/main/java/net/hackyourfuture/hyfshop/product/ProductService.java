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
        try {
            String key = fileService.upload(file);   // uploads to B2, returns the object key
            productRepository.setImageUrl(id, key);   // save the key in the DB
            return ProductResponse.from(productRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload product image", e);
        }
    }
    
    public ProductResponse deleteProductImage(int id) {
        Product product = productRepository.findById(id);
        String key = product.getImageUrl();
        if (key != null) {
        fileService.delete(key); 
    }
        productRepository.setImageUrl(id, null);   // clear the column
        return ProductResponse.from(productRepository.findById(id));
            
        }
}
