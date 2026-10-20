package ecommerce.database.adapter;

import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.database.SpringDataProductPriceRepository;
import ecommerce.database.model.ProductPriceJpaEntity;
import ecommerce.domain.model.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DatabasePricingAdapterTest {
    private SpringDataProductPriceRepository repository;
    private ProductPriceMapper mapper;
    private DatabasePricingAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(SpringDataProductPriceRepository.class);
        mapper = Mockito.mock(ProductPriceMapper.class);
        adapter = new DatabasePricingAdapter(repository, mapper);
    }

    @Test
    void should_return_price_when_found_in_database() {
        // Given
        Long productId = 1L;
        Long companyId = 1L;
        BigDecimal expectedPrice = new BigDecimal("2150.00");

        ProductPriceJpaEntity jpaEntity = new ProductPriceJpaEntity();
        ProductPrice domainModel = mock(ProductPrice.class);

        // 1. On stubbe le repository JPA
        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.of(jpaEntity));

        // 2. On stubbe le mapper pour qu'il renvoie un objet domaine valide
        when(mapper.toDomain(jpaEntity)).thenReturn(domainModel);

        // 3. On stubbe l'objet domaine pour qu'il retourne le prix attendu
        when(domainModel.getPriceHt()).thenReturn(expectedPrice);

        // When
        Optional<BigDecimal> result = adapter.getPriceForProductAndCompany(productId, companyId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualByComparingTo(expectedPrice);
        verify(repository, times(1)).findByProductIdAndCompanyId(productId, companyId);
    }

    @Test
    void should_return_empty_when_price_not_found() {
        // Given
        Long productId = 99L;
        Long companyId = null;

        when(repository.findByProductIdAndCompanyIdIsNull(productId))
                .thenReturn(Optional.empty());

        // When
        Optional<BigDecimal> result = adapter.getPriceForProductAndCompany(productId, companyId);

        // Then
        assertThat(result).isEmpty();
        verify(repository, times(1)).findByProductIdAndCompanyIdIsNull(productId);
    }
}
