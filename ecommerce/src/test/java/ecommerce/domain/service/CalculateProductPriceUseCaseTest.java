package ecommerce.domain.service;

import ecommerce.domain.model.UserContext;
import ecommerce.domain.port.PricingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CalculateProductPriceUseCaseTest {

    private PricingPort pricingPort;
    private CalculateProductPriceUseCase useCase;

    @BeforeEach
    void setUp() {
        pricingPort = Mockito.mock(PricingPort.class);
        useCase = new CalculateProductPriceUseCase(pricingPort);
    }

    @Test
    void should_query_null_company_when_user_is_b2c() {
        // Given : Un utilisateur B2C (pas de companyId)
        Long productId = 5L;
        UserContext b2cUser = new UserContext(1L, null);

        when(pricingPort.getPriceForProductAndCompany(productId, null))
                .thenReturn(Optional.of(new BigDecimal("100.00")));

        // When : On exécute le use case
        BigDecimal price = useCase.execute(productId, b2cUser);

        // Then : On vérifie que le prix est correct ET que le port a bien été appelé avec `null` (comportement B2C)
        assertThat(price).isEqualByComparingTo("100.00");
        verify(pricingPort, times(1)).getPriceForProductAndCompany(productId, null);
    }

    @Test
    void should_query_specific_company_when_user_is_b2b() {
        // Given : Un utilisateur B2B rattaché à l'entreprise 42
        Long productId = 5L;
        Long companyId = 42L;
        UserContext b2bUser = new UserContext(1L, companyId);

        when(pricingPort.getPriceForProductAndCompany(productId, companyId))
                .thenReturn(Optional.of(new BigDecimal("85.50")));

        // When : On exécute le use case
        BigDecimal price = useCase.execute(productId, b2bUser);

        // Then : On vérifie que le prix remisé est renvoyé ET que le port a bien reçu le companyId 42
        assertThat(price).isEqualByComparingTo("85.50");
        verify(pricingPort, times(1)).getPriceForProductAndCompany(productId, companyId);
    }
}