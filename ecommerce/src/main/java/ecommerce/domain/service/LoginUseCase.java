package ecommerce.domain.service;

import ecommerce.domain.model.User;
import ecommerce.domain.port.UserPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(UserPort userPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String email, String rawPassword) {
        // 1. Récupérer uniquement le hash du mot de passe via le port
        String passwordHash = userPort.findPasswordHashByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        // 2. Vérifier le mot de passe avec BCrypt
        if (!passwordEncoder.matches(rawPassword, passwordHash)) {
            throw new RuntimeException("Mot de passe invalide "+passwordHash);
        }

        // 3. Retourner le contexte utilisateur (sans jamais avoir manipulé le hash dans le domaine)
        return userPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
