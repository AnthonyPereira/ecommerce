package ecommerce.domain.service;

import ecommerce.domain.model.UserContext;
import ecommerce.domain.port.PricingPort;

import java.math.BigDecimal;

public class CalculateProductPriceUseCase {

    private final PricingPort pricingPort;

    public CalculateProductPriceUseCase(PricingPort pricingPort) {
        this.pricingPort = pricingPort;
    }

    public BigDecimal execute(Long productId, UserContext userContext) {
        // Extrait le companyId si l'utilisateur appartient à une entreprise (B2B), sinon null (B2C)
        Long companyId = (userContext != null) ? userContext.getCompanyId() : null;

        return pricingPort.getPriceForProductAndCompany(productId, companyId)
                .orElseThrow(() -> new IllegalStateException("Aucun prix trouvé pour le produit ID : " + productId));
    }
}
