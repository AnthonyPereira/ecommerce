package ecommerce.config;

import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.domain.port.PricingPort;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public CalculateProductPriceUseCase calculateProductPriceUseCase(PricingPort pricingPort) {
        return new CalculateProductPriceUseCase(pricingPort);
    }
}
