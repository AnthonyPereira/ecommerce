package ecommerce.database;

import ecommerce.database.model.ProductPriceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataProductPriceRepository extends JpaRepository<ProductPriceJpaEntity, Long> {

    // Recherche un prix négocié pour une entreprise spécifique (B2B)
    Optional<ProductPriceJpaEntity> findByProductIdAndCompanyId(Long productId, Long companyId);

    // Recherche le prix public par défaut (B2C, où company_id est NULL)
    Optional<ProductPriceJpaEntity> findByProductIdAndCompanyIdIsNull(Long productId);
}
