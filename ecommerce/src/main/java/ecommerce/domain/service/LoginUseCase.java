
package ecommerce.domain.service;

import ecommerce.domain.model.User;
import ecommerce.domain.port.UserPort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public LoginUseCase(
            UserPort userPort,
            PasswordEncoder passwordEncoder,
            LoginAttemptService loginAttemptService
    ) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    public User authenticate(String email, String rawPassword, String ip) {

        // 1. Vérifier si l'email ou l'IP est bloqué
        loginAttemptService.checkAttempts(email, ip);

        // 2. Récupérer le hash du mot de passe
        String passwordHash = userPort.findPasswordHashByEmail(email)
                .orElse(null);

        // 3. Vérifier les identifiants
        if (passwordHash == null
                || !passwordEncoder.matches(rawPassword, passwordHash)) {

            loginAttemptService.registerFailure(email, ip);

            throw new BadCredentialsException("Identifiants incorrects");
        }

        // 4. Récupérer le contexte utilisateur
        User user = userPort.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException("Identifiants incorrects")
                );

        // 5. Réinitialiser uniquement le compteur email
        loginAttemptService.resetEmailAttempts(email);

        return user;
    }
}
