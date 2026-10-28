
package ecommerce.api.security;

import ecommerce.domain.model.User;
import ecommerce.domain.port.UserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService {

    private final UserPort userPort;

    public AuthenticateUserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)
                || !authentication.isAuthenticated()) {
            throw new IllegalStateException(
                    "Aucun utilisateur authentifié"
            );
        }

        String subject = jwtAuth.getToken().getSubject();

        final Long userId;
        try {
            userId = Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Identifiant utilisateur invalide dans le JWT",
                    exception
            );
        }

        return userPort.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Utilisateur authentifié introuvable"
                ));
    }
}
