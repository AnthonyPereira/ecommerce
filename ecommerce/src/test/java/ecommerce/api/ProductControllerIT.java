package ecommerce.api;

import ecommerce.domain.model.UserContext;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@WebMvcTest(ProductController.class) // Démarre le contexte Spring MVC limité au contrôleur
@DisplayName("Test d'Intégration - ProductController API")
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Injecte un mock Spring du Use Case dans le contexte
    private CalculateProductPriceUseCase calculateProductPriceUseCase;

    @Test
    @DisplayName("IT - Doit retourner le prix public 2400.00 en B2C (sans header X-Company-Id)")
    void should_return_public_price_for_b2c_user() throws Exception {
        // Given
        Long productId = 1L;
        BigDecimal publicPrice = new BigDecimal("2400.00");

        when(calculateProductPriceUseCase.execute(eq(productId), any(UserContext.class)))
                .thenReturn(publicPrice);

        // When & Then
        mockMvc.perform(get("/products/{productId}/price", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("2400.00"));
    }

    @Test
    @DisplayName("IT - Doit transmettre le companyId et retourner le prix négocié B2B")
    void should_return_negotiated_price_for_b2b_company() throws Exception {
        // Given
        Long productId = 1L;
        Long companyId = 1L;
        BigDecimal negotiatedPrice = new BigDecimal("2150.00");

        when(calculateProductPriceUseCase.execute(eq(productId), any(UserContext.class)))
                .thenReturn(negotiatedPrice);

        // When & Then
        mockMvc.perform(get("/products/{productId}/price", productId)
                        .header("X-Company-Id", companyId))
                .andExpect(status().isOk())
                .andExpect(content().string("2150.00"));
    }
}
