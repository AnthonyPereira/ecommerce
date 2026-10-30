package ecommerce.api;

import ecommerce.api.dto.AuthResponseDto;
import ecommerce.api.dto.LoginRequestDto;
import ecommerce.domain.security.JwtService;
import ecommerce.domain.model.User;
import ecommerce.domain.service.LoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;

    public AuthController(LoginUseCase loginUseCase, JwtService jwtService) {
        this.loginUseCase = loginUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request, HttpServletRequest httpRequest) {

        User user = loginUseCase.authenticate(request.email(), request.password(), httpRequest.getRemoteAddr());

        String fullName = user.getFirstName() + " " + user.getLastName();

        String token = jwtService.generateToken(user);

        AuthResponseDto response = new AuthResponseDto(
                user.getId(),
                user.getEmail(),
                fullName,
                user.getCompanyId(),
                user.getRole().name(),
                token
        );

        return ResponseEntity.ok(response);
    }
}
