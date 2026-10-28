
package ecommerce.domain.security;

import ecommerce.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.expiration-seconds:3600}")
            long expirationSeconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "Impossible de générer un JWT : utilisateur null"
            );
        }

        if (user.getId() == null) {
            throw new IllegalArgumentException(
                    "Impossible de générer un JWT : Id utilisateur est null"
            );
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException(
                    "Le rôle de l'utilisateur est obligatoire"
            );
        }

        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("saas-achat")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name());

        if (user.getCompanyId() != null) {
            claimsBuilder.claim("companyId", user.getCompanyId());
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claimsBuilder.build())
        ).getTokenValue();
    }
}
