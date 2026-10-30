package ecommerce.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "login_attempts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_login_attempt_scope",
                        columnNames = {"scope_type", "identifier_hash"}
                )
        }
)
@Getter
@Setter
public class LoginAttemptJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scope_type", nullable = false, length = 10)
    private String scopeType;

    @Column(name = "identifier_hash", nullable = false, length = 64)
    private String identifierHash;

    @Column(name = "failed_attempts", nullable = false)
    private Integer failedAttempts = 0;

    @Column(name = "window_started_at", nullable = false)
    private OffsetDateTime windowStartedAt;

    @Column(name = "blocked_until")
    private OffsetDateTime blockedUntil;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
