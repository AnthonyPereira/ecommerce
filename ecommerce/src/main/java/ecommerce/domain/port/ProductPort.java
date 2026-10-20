package ecommerce.domain.port;

import ecommerce.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPort {
    List<Product> findAll();
    List<Product> findByIds(List<Long> productIds);
    Optional<Product> findBySku(String sku);
    List<Product> findByNameContaining(String name);
}
