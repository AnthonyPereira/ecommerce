
package ecommerce.database.adapter;

import ecommerce.database.ProductPriceRepository;
import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.database.model.ProductPriceJpaEntity;
import ecommerce.domain.model.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PricingAdapterTest {

    private ProductPriceRepository repository;
    private ProductPriceMapper mapper;
    private PricingAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProductPriceRepository.class);
        mapper = Mockito.mock(ProductPriceMapper.class);
        adapter = new PricingAdapter(repository, mapper);
    }

    @Test
    void should_return_company_specific_price_when_found() {
        Long productId = 1L;
        Long companyId = 10L;
        BigDecimal expectedPrice = new BigDecimal("2150.00");

        ProductPriceJpaEntity entity = new ProductPriceJpaEntity();
        ProductPrice domainModel = mock(ProductPrice.class);

        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domainModel);
        when(domainModel.getPriceHt()).thenReturn(expectedPrice);

        Optional<BigDecimal> result =
                adapter.getPriceForProductAndCompany(productId, companyId);

        assertThat(result).contains(expectedPrice);
        verify(repository).findByProductIdAndCompanyId(productId, companyId);
        verify(repository, never())
                .findByProductIdAndCompanyIdIsNull(productId);
    }

    @Test
    void should_fallback_to_public_price_when_company_price_is_missing() {
        Long productId = 1L;
        Long companyId = 10L;
        BigDecimal publicPrice = new BigDecimal("100.00");

        ProductPriceJpaEntity publicEntity = new ProductPriceJpaEntity();
        ProductPrice publicModel = mock(ProductPrice.class);

        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.empty());
        when(repository.findByProductIdAndCompanyIdIsNull(productId))
                .thenReturn(Optional.of(publicEntity));
        when(mapper.toDomain(publicEntity)).thenReturn(publicModel);
        when(publicModel.getPriceHt()).thenReturn(publicPrice);

        Optional<BigDecimal> result =
                adapter.getPriceForProductAndCompany(productId, companyId);

        assertThat(result).contains(publicPrice);
        verify(repository).findByProductIdAndCompanyId(productId, companyId);
        verify(repository).findByProductIdAndCompanyIdIsNull(productId);
    }

    @Test
    void should_return_empty_when_company_and_public_prices_are_missing() {
        Long productId = 99L;
        Long companyId = 10L;

        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.empty());
        when(repository.findByProductIdAndCompanyIdIsNull(productId))
                .thenReturn(Optional.empty());

        Optional<BigDecimal> result =
                adapter.getPriceForProductAndCompany(productId, companyId);

        assertThat(result).isEmpty();
        verify(repository).findByProductIdAndCompanyId(productId, companyId);
        verify(repository).findByProductIdAndCompanyIdIsNull(productId);
    }

    @Test
    void should_return_empty_when_public_price_is_missing_for_anonymous_user() {
        Long productId = 99L;

        when(repository.findByProductIdAndCompanyIdIsNull(productId))
                .thenReturn(Optional.empty());

        Optional<BigDecimal> result =
                adapter.getPriceForProductAndCompany(productId, null);

        assertThat(result).isEmpty();
        verify(repository).findByProductIdAndCompanyIdIsNull(productId);
        verify(repository, never())
                .findByProductIdAndCompanyId(anyLong(), anyLong());
    }
}
