package ecommerce.database;

import ecommerce.database.model.ProductPriceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPriceJpaEntity, Long> {

    Optional<ProductPriceJpaEntity> findByProductIdAndCompanyId(Long productId, Long companyId);

    Optional<ProductPriceJpaEntity> findByProductIdAndCompanyIdIsNull(Long productId);

    List<ProductPriceJpaEntity> findByCompanyIdIsNull();

    List<ProductPriceJpaEntity> findByCompanyId(Long companyId);
}
