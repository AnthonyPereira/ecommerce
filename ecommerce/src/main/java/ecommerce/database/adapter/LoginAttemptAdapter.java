package ecommerce.database.adapter;

import ecommerce.database.LoginAttemptRepository;
import ecommerce.database.exception.LoginAttemptCreationConflictException;
import ecommerce.database.model.LoginAttemptJpaEntity;
import ecommerce.domain.model.LoginAttemptData;
import ecommerce.domain.port.LoginAttemptPort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class LoginAttemptAdapter implements LoginAttemptPort {

    private static final int MAX_CREATION_RETRIES = 3;

    private final LoginAttemptRepository repository;
    private final LoginAttemptTransactionAdapter transactionAdapter;

    public LoginAttemptAdapter(
            LoginAttemptRepository repository,
            LoginAttemptTransactionAdapter transactionAdapter
    ) {
        this.repository = repository;
        this.transactionAdapter = transactionAdapter;
    }

    @Override
    public Optional<LoginAttemptData> findByScopeAndIdentifier(
            String scopeType,
            String identifierHash
    ) {
        return repository
                .findByScopeTypeAndIdentifierHash(scopeType, identifierHash)
                .map(this::toDomain);
    }

    @Override
    public LoginAttemptData registerFailure(
            String scopeType,
            String identifierHash,
            int maxAttempts,
            Duration window,
            Duration blockDuration,
            OffsetDateTime now
    ) {
        for (int retry = 0; retry < MAX_CREATION_RETRIES; retry++) {
            try {
                return transactionAdapter.registerFailure(
                        scopeType,
                        identifierHash,
                        maxAttempts,
                        window,
                        blockDuration,
                        now
                );
            } catch (LoginAttemptCreationConflictException exception) {
                if (retry == MAX_CREATION_RETRIES - 1) {
                    throw exception;
                }
            }
        }

        throw new IllegalStateException(
                "Impossible d'enregistrer la tentative de connexion."
        );
    }

    @Override
    public void deleteByScopeAndIdentifier(
            String scopeType,
            String identifierHash
    ) {
        repository.deleteByScopeTypeAndIdentifierHash(
                scopeType,
                identifierHash
        );
    }

    private LoginAttemptData toDomain(LoginAttemptJpaEntity entity) {
        return new LoginAttemptData(
                entity.getId(),
                entity.getScopeType(),
                entity.getIdentifierHash(),
                entity.getFailedAttempts(),
                entity.getWindowStartedAt(),
                entity.getBlockedUntil(),
                entity.getUpdatedAt()
        );
    }
}