package ecommerce.config;

import ecommerce.database.mapper.ProductPriceMapper;
import ecommerce.domain.port.LoginAttemptPort;
import ecommerce.domain.port.PricingPort;
import ecommerce.domain.port.ProductPort;
import ecommerce.domain.port.UserPort;
import ecommerce.domain.security.IdentifierHashService;
import ecommerce.domain.service.CalculateProductPriceUseCase;
import ecommerce.domain.service.FindProductsUseCase;
import ecommerce.domain.service.LoginAttemptService;
import ecommerce.domain.service.LoginUseCase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public LoginUseCase loginUseCase(UserPort userPort, PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService){
        return new LoginUseCase(userPort, passwordEncoder, loginAttemptService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginAttemptService loginAttemptService(LoginAttemptPort loginAttemptPort,
                                                   IdentifierHashService identifierHashService,
                                                   LoginAttemptProperties loginAttemptProperties){
        return new LoginAttemptService(loginAttemptPort, identifierHashService, loginAttemptProperties);
    }

    @Bean
    public IdentifierHashService identifierHashService(LoginAttemptProperties loginAttemptProperties){
        return new IdentifierHashService(loginAttemptProperties);
    }
}
