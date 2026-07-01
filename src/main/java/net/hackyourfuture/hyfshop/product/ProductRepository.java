package net.hackyourfuture.hyfshop.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.hackyourfuture.hyfshop.product.dto.ProductResponse;
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
                .sql("SELECT * FROM products ORDER BY id")
                .query(PRODUCT_ROW_MAPPER)
                .list();

    }

    public Product findById(int id) {
        return jdbcClient
                .sql("SELECT * FROM products WHERE id = :id")
                .param("id", id)
                .query(PRODUCT_ROW_MAPPER)
                .single();
    }

    public Product setImageUrl(int id, String imageUrl) {
        return jdbcClient.sql("""
                        UPDATE products
                        SET image_url = :imageUrl
                        WHERE id = :id
                        RETURNING *
                        """)
                .param("imageUrl", imageUrl)
                .param("id", id)
                .query(PRODUCT_ROW_MAPPER)
                .single();
    }

    public List<Product> findByColor(String color) {
        return jdbcClient.sql("""
                SELECT * FROM products
                WHERE details->>'color' = :color
                OR details ->'colors' @> :colorJson::jsonb
                ORDER BY id
                """)
                .param("color", color)
                .param("colorJson", "\"" + color + "\"")
                .query(PRODUCT_ROW_MAPPER)
                .list();
    }

    public Product setSize(int id, String size) {
        return jdbcClient.sql("""
                UPDATE products
                SET details = jsonb_set(details, '{size}', :size::jsonb)
                WHERE id = :id
                RETURNING *
                """)
                .param("size", "\"" + size + "\"")
                .param("id", id)
                .query(PRODUCT_ROW_MAPPER)
                .single();
    }
}
