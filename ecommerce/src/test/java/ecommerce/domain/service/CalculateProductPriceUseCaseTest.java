
package ecommerce.domain.service;

import ecommerce.domain.model.Role;
import ecommerce.domain.model.User;
import ecommerce.domain.port.PricingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Long productId = 5L;
        User b2cUser = new User(
                1L, null, "firstName", "lastName",
                "mail@mail.fr", Role.USER
        );

        when(pricingPort.getPriceForProductAndCompany(productId, null))
                .thenReturn(Optional.of(new BigDecimal("100.00")));

        BigDecimal price = useCase.execute(productId, b2cUser);

        assertThat(price).isEqualByComparingTo("100.00");
        verify(pricingPort).getPriceForProductAndCompany(productId, null);
        verifyNoMoreInteractions(pricingPort);
    }

    @Test
    void should_query_specific_company_when_user_is_b2b() {
        Long productId = 5L;
        Long companyId = 42L;
        User b2bUser = new User(
                1L, companyId, "firstName", "lastName",
                "mail@mail.fr", Role.PRO
        );

        when(pricingPort.getPriceForProductAndCompany(productId, companyId))
                .thenReturn(Optional.of(new BigDecimal("85.50")));

        BigDecimal price = useCase.execute(productId, b2bUser);

        assertThat(price).isEqualByComparingTo("85.50");
        verify(pricingPort).getPriceForProductAndCompany(productId, companyId);
        verifyNoMoreInteractions(pricingPort);
    }

    @Test
    void should_query_public_price_when_user_is_anonymous() {
        Long productId = 5L;

        when(pricingPort.getPriceForProductAndCompany(productId, null))
                .thenReturn(Optional.of(new BigDecimal("100.00")));

        BigDecimal price = useCase.execute(productId, null);

        assertThat(price).isEqualByComparingTo("100.00");
        verify(pricingPort).getPriceForProductAndCompany(productId, null);
        verifyNoMoreInteractions(pricingPort);
    }

    @Test
    void should_reject_pro_user_without_company() {
        User proUser = new User(
                1L, null, "firstName", "lastName",
                "mail@mail.fr", Role.PRO
        );

        assertThatThrownBy(() -> useCase.execute(5L, proUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Un utilisateur PRO doit être rattaché à une entreprise"
                );

        verifyNoInteractions(pricingPort);
    }

    @Test
    void should_query_public_price_when_user_is_admin() {
        Long productId = 5L;
        User admin = new User(
                1L, null, "Admin", "User",
                "admin@mail.fr", Role.ADMIN
        );

        when(pricingPort.getPriceForProductAndCompany(productId, null))
                .thenReturn(Optional.of(new BigDecimal("100.00")));

        BigDecimal price = useCase.execute(productId, admin);

        assertThat(price).isEqualByComparingTo("100.00");
        verify(pricingPort).getPriceForProductAndCompany(productId, null);
        verifyNoMoreInteractions(pricingPort);
    }

    @Test
    void should_throw_when_no_price_is_found() {
        Long productId = 5L;
        User b2cUser = new User(
                1L, null, "firstName", "lastName",
                "mail@mail.fr", Role.USER
        );

        when(pricingPort.getPriceForProductAndCompany(productId, null))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(productId, b2cUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Aucun prix trouvé pour le produit ID : 5");

        verify(pricingPort).getPriceForProductAndCompany(productId, null);
        verifyNoMoreInteractions(pricingPort);
    }
}
