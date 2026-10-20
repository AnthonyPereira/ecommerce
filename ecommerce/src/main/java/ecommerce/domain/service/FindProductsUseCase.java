package ecommerce.domain.service;

import ecommerce.domain.model.Product;
import ecommerce.domain.port.ProductPort;

import java.util.List;
import java.util.Optional;

public class FindProductsUseCase {

    private final ProductPort productPort;

    public FindProductsUseCase(ProductPort productPort) {
        this.productPort = productPort;
    }

    // 1. Récupérer tout le catalogue
    public List<Product> executeAll() {
        return productPort.findAll();
    }

    // 2. Récupérer par une liste d'IDs
    public List<Product> executeByIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return productPort.findByIds(productIds);
    }

    // 3. Récupérer un produit unique par son SKU
    public Optional<Product> executeBySku(String sku) {
        return productPort.findBySku(sku);
    }

    // 4. Rechercher des produits par leur nom (recherche textuelle)
    public List<Product> executeByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return productPort.findByNameContaining(name);
    }
}