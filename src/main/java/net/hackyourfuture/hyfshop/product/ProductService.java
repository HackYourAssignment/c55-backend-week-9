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

    public ProductResponse getProductById(int id) {
        return ProductResponse.from(productRepository.findById(id));
    }

    public ProductResponse setProductSize(int id, String size) {
        return ProductResponse.from(productRepository.setSize(id, size));
    }

    public ProductResponse setProductImage(int id, MultipartFile file) throws Exception {
        String url = fileService.upload(file);
       return ProductResponse.from(productRepository.setImageUrl(id, url));
    }

    public ProductResponse deleteProductImage(int id) {
        Product product = productRepository.findById(id);

        if(product.getImageUrl() != null) {
            fileService.delete(product.getImageUrl());
        }

        return ProductResponse.from(productRepository.setImageUrl(id, null));
    }
}
