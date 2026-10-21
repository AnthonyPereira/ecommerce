package ecommerce.api;


import ecommerce.api.dto.ProductResponseDto;
import ecommerce.domain.model.Product;
import ecommerce.domain.model.User;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CalculateProductPriceUseCase calculateProductPriceUseCase;
    private final FindProductsUseCase findProductsUseCase;

    public ProductController(CalculateProductPriceUseCase calculateProductPriceUseCase,
                             FindProductsUseCase findProductsUseCase) {
        this.calculateProductPriceUseCase = calculateProductPriceUseCase;
        this.findProductsUseCase = findProductsUseCase;
    }

    @GetMapping("/{productId}/price")
    public ResponseEntity<BigDecimal> getProductPrice(
            @PathVariable Long productId,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId) { // Simule l'entreprise de l'utilisateur connecté

        // Construction du contexte utilisateur basé sur l'authentification
        User user = new User(100L, companyId, "firstName", "lastName", "mail@email.fr");

        BigDecimal price = calculateProductPriceUseCase.execute(productId, user);
        return ResponseEntity.ok(price);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> ids) {

        List<Product> products;

        // Aiguillage selon les paramètres de la requête
        if (sku != null && !sku.isBlank()) {
            return findProductsUseCase.executeBySku(sku)
                    .map(p -> ResponseEntity.ok(List.of(ProductResponseDto.fromDomain(p))))
                    .orElse(ResponseEntity.notFound().build());
        } else if (name != null && !name.isBlank()) {
            products = findProductsUseCase.executeByName(name);
        } else if (ids != null && !ids.isEmpty()) {
            products = findProductsUseCase.executeByIds(ids);
        } else {
            products = findProductsUseCase.executeAll();
        }

        // Conversion de la liste du domaine vers la liste de DTOs
        List<ProductResponseDto> response = products.stream()
                .map(ProductResponseDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
