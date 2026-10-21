package ecommerce.api;

import ecommerce.api.dto.AuthResponseDto;
import ecommerce.api.dto.LoginRequestDto;
import ecommerce.domain.model.User;
import ecommerce.domain.service.LoginUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {

        User user = loginUseCase.authenticate(request.email(), request.password());

        String fullName = user.getFirstName() + " " + user.getLastName();

        AuthResponseDto response = new AuthResponseDto(
                user.getUserId(),
                user.getEmail(),
                fullName,
                user.getCompanyId(),
                "dummy-jwt-token-xyz" // Jeton factice en attendant d'ajouter Spring Security JWT
        );

        return ResponseEntity.ok(response);
    }
}
