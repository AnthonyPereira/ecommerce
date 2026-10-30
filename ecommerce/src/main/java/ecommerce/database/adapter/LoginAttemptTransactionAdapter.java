package ecommerce.database.adapter;

import ecommerce.database.LoginAttemptRepository;
import ecommerce.database.exception.LoginAttemptCreationConflictException;
import ecommerce.database.model.LoginAttemptJpaEntity;
import ecommerce.domain.model.LoginAttemptData;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

@Component
public class LoginAttemptTransactionAdapter {

    private final LoginAttemptRepository repository;

    public LoginAttemptTransactionAdapter(
            LoginAttemptRepository repository,
            EntityManager entityManager
    ) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LoginAttemptData registerFailure(
            String scopeType,
            String identifierHash,
            int maxAttempts,
            Duration window,
            Duration blockDuration,
            OffsetDateTime now
    ) {
        LoginAttemptJpaEntity attempt = repository
                .findForUpdate(scopeType, identifierHash)
                .orElse(null);

        if (attempt == null) {
            attempt = new LoginAttemptJpaEntity();

            attempt.setScopeType(scopeType);
            attempt.setIdentifierHash(identifierHash);
            attempt.setFailedAttempts(1);
            attempt.setWindowStartedAt(now);
            attempt.setBlockedUntil(
                    maxAttempts <= 1 ? now.plus(blockDuration) : null
            );
            attempt.setUpdatedAt(now);

            try {
                repository.saveAndFlush(attempt);
                return toDomain(attempt);
            } catch (DataIntegrityViolationException exception) {
                throw new LoginAttemptCreationConflictException(exception);
            }
        }

        boolean windowExpired = !attempt.getWindowStartedAt()
                .plus(window)
                .isAfter(now);

        if (windowExpired) {
            attempt.setFailedAttempts(1);
            attempt.setWindowStartedAt(now);
            attempt.setBlockedUntil(
                    maxAttempts <= 1 ? now.plus(blockDuration) : null
            );
        } else {
            int failedAttempts = attempt.getFailedAttempts() + 1;

            attempt.setFailedAttempts(failedAttempts);

            if (failedAttempts >= maxAttempts) {
                attempt.setBlockedUntil(now.plus(blockDuration));
            }
        }

        attempt.setUpdatedAt(now);

        repository.saveAndFlush(attempt);

        return toDomain(attempt);
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