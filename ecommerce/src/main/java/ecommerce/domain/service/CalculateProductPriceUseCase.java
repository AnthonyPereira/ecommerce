
package ecommerce.domain.service;

import ecommerce.domain.model.User;
import ecommerce.domain.port.PricingPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateProductPriceUseCase {

    private final PricingPort pricingPort;

    public CalculateProductPriceUseCase(PricingPort pricingPort) {
        this.pricingPort = pricingPort;
    }

    public BigDecimal execute(Long productId, User user) {
        Long companyId = resolveCompanyId(user);

        return pricingPort.getPriceForProductAndCompany(productId, companyId)
                .orElseThrow(() -> new IllegalStateException(
                        "Aucun prix trouvé pour le produit ID : " + productId
                ));
    }

    private Long resolveCompanyId(User user) {
        // Visiteur anonyme : prix public
        if (user == null) {
            return null;
        }

        if (user.getRole() == null) {
            throw new IllegalStateException(
                    "Le rôle de l'utilisateur est obligatoire"
            );
        }

        return switch (user.getRole()) {
            case USER -> null; // Particulier : prix public uniquement

            case PRO -> {
                if (user.getCompanyId() == null) {
                    throw new IllegalStateException(
                            "Un utilisateur PRO doit être rattaché à une entreprise"
                    );
                }
                yield user.getCompanyId();
            }

            case ADMIN -> null; // Par défaut : prix public
        };
    }
}
