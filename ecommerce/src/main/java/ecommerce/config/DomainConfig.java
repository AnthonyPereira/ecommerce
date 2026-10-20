package ecommerce.config;

import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.domain.port.PricingPort;
import ecommerce.domain.port.ProductPort;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public CalculateProductPriceUseCase calculateProductPriceUseCase(PricingPort pricingPort) {
        return new CalculateProductPriceUseCase(pricingPort);
    }

    @Bean
    public FindProductsUseCase findProductsUseCase(ProductPort productPort){
        return new FindProductsUseCase(productPort);
    }
}
