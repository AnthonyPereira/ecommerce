package ecommerce.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPriceTest {
    @Test
    void should_test_getters_setters_and_constructors() {
        // Given & When
        ProductPrice productPrice = new ProductPrice(1L, 1L, 1L,new BigDecimal("500.00"));

        // Then
        assertThat(productPrice.getId()).isEqualTo(1L);
        assertThat(productPrice.getProductId()).isEqualTo(1L);
        assertThat(productPrice.getCompanyId()).isEqualTo(1L);
        assertThat(productPrice.getPriceHt()).isEqualByComparingTo("500.00");
    }
}
