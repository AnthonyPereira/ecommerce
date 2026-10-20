package ecommerce.api;

import ecommerce.domain.model.UserContext;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Find;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    private CalculateProductPriceUseCase calculateProductPriceUseCase;
    private FindProductsUseCase findProductsUseCase;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        // On mock le use case
        calculateProductPriceUseCase = Mockito.mock(CalculateProductPriceUseCase.class);
        findProductsUseCase = Mockito.mock(FindProductsUseCase.class);
        // On instancie directement le contrôleur en lui injectant le mock
        productController = new ProductController(calculateProductPriceUseCase, findProductsUseCase);
    }

    @Test
    void should_return_product_price() {
        // Given
        Long productId = 1L;
        Long companyId = 10L;
        BigDecimal expectedPrice = new BigDecimal("2150.00");

        when(calculateProductPriceUseCase.execute(eq(productId), any(UserContext.class)))
                .thenReturn(expectedPrice);

        // When (Appel direct de la méthode du contrôleur)
        BigDecimal price = productController.getProductPrice(productId, companyId).getBody();

        // Then
        assertThat(price).isEqualByComparingTo(expectedPrice);
    }
}