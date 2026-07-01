package net.hackyourfuture.hyfshop.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ProductRepository {
    private final JdbcClient jdbcClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, _) -> {
        var product = new Product();
        product.setId(rs.getInt("id"));
        product.setTitle(rs.getString("title"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));

        try{
          String json = rs.getString("details");
          if(json != null){
              product.setDetails(objectMapper.readValue(json,
                      new TypeReference<Map<String, Object>>() {
                      }));
          }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return product;
    };

    public List<Product> getAllProducts() {
        return jdbcClient
                .sql("SELECT * FROM products")
                .query(PRODUCT_ROW_MAPPER)
                .list();

    }

    public Product findById(int id) {
        return jdbcClient
                .sql("SELECT id, title, price, category, image_url FROM products WHERE id = :id")
                .param("id", id)
                .query(PRODUCT_ROW_MAPPER)
                .single();
    }

    public void setImageUrl(int id, String imageUrl) {
        jdbcClient.sql("""
                        UPDATE products
                        SET image_url = :imageUrl
                        WHERE id = :id
                        """)
                .param("id", id)
                .param("imageUrl", imageUrl)
                .update();
    }

    public List<Product> findByColor(String color) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Product setSize(int id, String size) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
