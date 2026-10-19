package ecommerce.domain.port;

import java.math.BigDecimal;
import java.util.Optional;

public interface PricingPort {
    Optional<BigDecimal> getPriceForProductAndCompany(Long productId, Long companyId);
}
