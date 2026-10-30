package ecommerce.domain.security;

import ecommerce.config.LoginAttemptProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class IdentifierHashService {
    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec secretKey;

    public IdentifierHashService(LoginAttemptProperties properties) {
        byte[] key = Base64.getDecoder().decode(properties.hmacSecret());

        if (key.length < 32) {
            throw new IllegalStateException(
                    "La clé HMAC doit contenir au moins 32 octets."
            );
        }

        this.secretKey = new SecretKeySpec(key, ALGORITHM);
    }

    public String hash(String identifier) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] hash = mac.doFinal(
                    identifier.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Impossible de calculer l'empreinte HMAC.",
                    exception
            );
        }
    }
}
