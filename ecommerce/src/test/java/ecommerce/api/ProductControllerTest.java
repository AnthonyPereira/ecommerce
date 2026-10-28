package ecommerce.api;

import ecommerce.api.security.AuthenticateUserService;
import ecommerce.domain.model.User;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    private CalculateProductPriceUseCase calculateProductPriceUseCase;
    private FindProductsUseCase findProductsUseCase;
    private AuthenticateUserService authenticateUserService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        // On mock le use case
        calculateProductPriceUseCase = Mockito.mock(CalculateProductPriceUseCase.class);
        findProductsUseCase = Mockito.mock(FindProductsUseCase.class);
        authenticateUserService = Mockito.mock(AuthenticateUserService.class);
        // On instancie directement le contrôleur en lui injectant le mock
        productController = new ProductController(calculateProductPriceUseCase, findProductsUseCase, authenticateUserService);
    }

}