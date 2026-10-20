package ecommerce.database;

import ecommerce.database.model.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, Long> {
    Optional<ProductJpaEntity> findBySku(String sku);
    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);
}
