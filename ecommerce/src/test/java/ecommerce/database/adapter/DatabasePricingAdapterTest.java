package ecommerce.database.adapter;

import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.database.SpringDataProductPriceRepository;
import ecommerce.database.model.ProductPriceJpaEntity;
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
        // On mock le repository JPA et le mapper MapStruct
        repository = Mockito.mock(SpringDataProductPriceRepository.class);
        mapper = Mockito.mock(ProductPriceMapper.class);

        // On instancie l'adaptateur en lui injectant les faux objets
        adapter = new DatabasePricingAdapter(repository, mapper);
    }

    @Test
    void should_return_price_when_found_in_database() {
        // Given
        Long productId = 1L;
        Long companyId = 1L;
        BigDecimal expectedPrice = new BigDecimal("2150.00");

        // On simule que le repository trouve une entité en base
        // (Adaptez la méthode selon le nom exact de votre repository Spring Data)

        ProductPriceJpaEntity jpaEntity = new ProductPriceJpaEntity();
        jpaEntity.setPriceHt(expectedPrice);

        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.of(jpaEntity));

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

        when(repository.findByProductIdAndCompanyId(productId, companyId))
                .thenReturn(Optional.empty());

        // When
        Optional<BigDecimal> result = adapter.getPriceForProductAndCompany(productId, companyId);

        // Then
        assertThat(result).isEmpty();
        verify(repository, times(1)).findByProductIdAndCompanyId(productId, companyId);
    }
}
