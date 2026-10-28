
package ecommerce.api;

import ecommerce.api.dto.ProductResponseDto;
import ecommerce.api.security.AuthenticateUserService;
import ecommerce.domain.model.Product;
import ecommerce.domain.model.User;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CalculateProductPriceUseCase calculateProductPriceUseCase;
    private final FindProductsUseCase findProductsUseCase;
    private final AuthenticateUserService authenticatedUserService;

    public ProductController(
            CalculateProductPriceUseCase calculateProductPriceUseCase,
            FindProductsUseCase findProductsUseCase,
            AuthenticateUserService authenticatedUserService
    ) {
        this.calculateProductPriceUseCase = calculateProductPriceUseCase;
        this.findProductsUseCase = findProductsUseCase;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> ids,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // Résoudre l'utilisateur une seule fois pour toute la requête.
        User user = jwt != null
                ? authenticatedUserService.getCurrentUser()
                : null;

        if (sku != null && !sku.isBlank()) {
            return findProductsUseCase.executeBySku(sku)
                    .map(product -> ResponseEntity.ok(
                            List.of(toDto(product, user))
                    ))
                    .orElse(ResponseEntity.notFound().build());
        }

        List<Product> products;

        if (name != null && !name.isBlank()) {
            products = findProductsUseCase.executeByName(name);
        } else if (ids != null && !ids.isEmpty()) {
            products = findProductsUseCase.executeByIds(ids);
        } else {
            products = findProductsUseCase.executeAll();
        }

        List<ProductResponseDto> response = products.stream()
                .map(product -> toDto(product, user))
                .toList();

        return ResponseEntity.ok(response);
    }

    private ProductResponseDto toDto(Product product, User user) {
        BigDecimal displayedPrice =
                calculateProductPriceUseCase.execute(product.getId(), user);

        return ProductResponseDto.fromDomain(product, displayedPrice);
    }
}
