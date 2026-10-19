package ecommerce.api;


import ecommerce.domain.model.UserContext;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CalculateProductPriceUseCase calculateProductPriceUseCase;

    public ProductController(CalculateProductPriceUseCase calculateProductPriceUseCase) {
        this.calculateProductPriceUseCase = calculateProductPriceUseCase;
    }

    @GetMapping("/{productId}/price")
    public ResponseEntity<BigDecimal> getProductPrice(
            @PathVariable Long productId,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId) { // Simule l'entreprise de l'utilisateur connecté

        // Construction du contexte utilisateur basé sur l'authentification
        UserContext userContext = new UserContext(100L, companyId);

        BigDecimal price = calculateProductPriceUseCase.execute(productId, userContext);
        return ResponseEntity.ok(price);
    }
}
