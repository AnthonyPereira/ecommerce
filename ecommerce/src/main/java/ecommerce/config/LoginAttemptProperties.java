package ecommerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.login-attempts")
public record LoginAttemptProperties(
        Email email,
        Ip ip,
        Duration window,
        Duration blockDuration,
        String hmacSecret
) {
    public record Email(int maxAttempts) {}
    public record Ip(int maxAttempts) {}
}
