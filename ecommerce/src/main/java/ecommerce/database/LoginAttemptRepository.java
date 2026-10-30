package ecommerce.database;

import ecommerce.database.model.LoginAttemptJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginAttemptRepository
        extends JpaRepository<LoginAttemptJpaEntity, Long> {

    Optional<LoginAttemptJpaEntity> findByScopeTypeAndIdentifierHash(
            String scopeType,
            String identifierHash
    );

    boolean existsByScopeTypeAndIdentifierHashAndBlockedUntilAfter(
            String scopeType,
            String identifierHash,
            java.time.OffsetDateTime now
    );

    void deleteByScopeTypeAndIdentifierHash(
            String scopeType,
            String identifierHash
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT attempt
            FROM LoginAttemptJpaEntity attempt
            WHERE attempt.scopeType = :scopeType
            AND attempt.identifierHash = :identifierHash
            """)
    Optional<LoginAttemptJpaEntity> findForUpdate(
            @Param("scopeType") String scopeType,
            @Param("identifierHash") String identifierHash
    );
}
