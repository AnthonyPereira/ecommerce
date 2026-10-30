package ecommerce.domain.service;

import ecommerce.config.LoginAttemptProperties;
import ecommerce.domain.exception.LoginBlockedException;
import ecommerce.domain.model.LoginAttemptData;
import ecommerce.domain.port.LoginAttemptPort;
import ecommerce.domain.security.IdentifierHashService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Locale;

@Service
public class LoginAttemptService {

    private static final String EMAIL_SCOPE = "EMAIL";
    private static final String IP_SCOPE = "IP";

    private final LoginAttemptPort loginAttemptPort;
    private final IdentifierHashService identifierHashService;
    private final LoginAttemptProperties properties;

    public LoginAttemptService(
            LoginAttemptPort loginAttemptPort,
            IdentifierHashService identifierHashService,
            LoginAttemptProperties properties
    ) {
        this.loginAttemptPort = loginAttemptPort;
        this.identifierHashService = identifierHashService;
        this.properties = properties;
    }

    public void checkAttempts(String email, String ip) {
        String emailHash = hashEmail(email);
        String ipHash = hashIp(ip);

        checkScope(
                EMAIL_SCOPE,
                emailHash,
                properties.email().maxAttempts()
        );

        checkScope(
                IP_SCOPE,
                ipHash,
                properties.ip().maxAttempts()
        );
    }

    public void registerFailure(String email, String ip) {
        String emailHash = hashEmail(email);
        String ipHash = hashIp(ip);

        OffsetDateTime now = OffsetDateTime.now();

        registerFailureForScope(
                EMAIL_SCOPE,
                emailHash,
                properties.email().maxAttempts(),
                now
        );

        registerFailureForScope(
                IP_SCOPE,
                ipHash,
                properties.ip().maxAttempts(),
                now
        );
    }

    public void resetEmailAttempts(String email) {
        String emailHash = hashEmail(email);

        loginAttemptPort.deleteByScopeAndIdentifier(
                EMAIL_SCOPE,
                emailHash
        );
    }

    private void checkScope(
            String scope,
            String identifierHash,
            int maxAttempts
    ) {
        loginAttemptPort.findByScopeAndIdentifier(scope, identifierHash)
                .ifPresent(attempt -> {
                    OffsetDateTime now = OffsetDateTime.now();

                    boolean activelyBlocked =
                            attempt.blockedUntil() != null
                                    && attempt.blockedUntil().isAfter(now);

                    boolean limitReached =
                            attempt.windowStartedAt()
                                    .plus(properties.window())
                                    .isAfter(now)
                                    && attempt.failedAttempts() >= maxAttempts;

                    if (activelyBlocked || limitReached) {
                        throw new LoginBlockedException();
                    }
                });
    }

    private void registerFailureForScope(
            String scope,
            String identifierHash,
            int maxAttempts,
            OffsetDateTime now
    ) {
        LoginAttemptData attempt = loginAttemptPort.registerFailure(
                scope,
                identifierHash,
                maxAttempts,
                properties.window(),
                properties.blockDuration(),
                now
        );

        if (attempt.blockedUntil() != null
                && attempt.blockedUntil().isAfter(now)) {
            throw new LoginBlockedException();
        }
    }

    private String hashEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        return identifierHashService.hash(normalizedEmail);
    }

    private String hashIp(String ip) {
        return identifierHashService.hash(ip);
    }
}