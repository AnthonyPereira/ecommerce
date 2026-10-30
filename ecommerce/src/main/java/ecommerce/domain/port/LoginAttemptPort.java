package ecommerce.domain.port;

import ecommerce.domain.model.LoginAttemptData;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LoginAttemptPort {

    Optional<LoginAttemptData> findByScopeAndIdentifier(
            String scopeType,
            String identifierHash
    );

    LoginAttemptData registerFailure(
            String scopeType,
            String identifierHash,
            int maxAttempts,
            Duration window,
            Duration blockDuration,
            OffsetDateTime now
    );

    void deleteByScopeAndIdentifier(
            String scopeType,
            String identifierHash
    );
}