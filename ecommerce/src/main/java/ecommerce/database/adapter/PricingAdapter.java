package ecommerce.database.adapter;

import ecommerce.database.ProductPriceRepository;
import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.database.model.ProductPriceJpaEntity;
import ecommerce.domain.model.ProductPrice;
import ecommerce.domain.port.PricingPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PricingAdapter implements PricingPort {

    private final ProductPriceRepository repository;
    private final ProductPriceMapper mapper;

    public PricingAdapter(ProductPriceRepository repository, ProductPriceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<BigDecimal> getPriceForProductAndCompany(Long productId, Long companyId) {
        if (companyId != null) {
            Optional<ProductPriceJpaEntity> entity = repository.findByProductIdAndCompanyId(productId, companyId);
            if (entity.isPresent()) {
                // Utilisation du mapper généré par MapStruct
                ProductPrice domainModel = mapper.toDomain(entity.get());
                return Optional.of(domainModel.getPriceHt());
            }
        }

        return repository.findByProductIdAndCompanyIdIsNull(productId)
                .map(entity -> mapper.toDomain(entity).getPriceHt());
    }
}
