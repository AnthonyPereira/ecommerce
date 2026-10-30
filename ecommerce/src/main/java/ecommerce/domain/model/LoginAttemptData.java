package ecommerce.domain.model;

import java.time.OffsetDateTime;

public record LoginAttemptData(
        Long id,
        String scopeType,
        String identifierHash,
        Integer failedAttempts,
        OffsetDateTime windowStartedAt,
        OffsetDateTime blockedUntil,
        OffsetDateTime updatedAt
) {}
