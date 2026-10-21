package ecommerce.domain.port;

import ecommerce.domain.model.User;

import java.util.Optional;

public interface UserPort {
    Optional<User> findByEmail(String email);

    Optional<String> findPasswordHashByEmail(String email);
}
