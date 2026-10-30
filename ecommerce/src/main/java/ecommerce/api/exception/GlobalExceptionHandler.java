package ecommerce.api.exception;

import ecommerce.domain.exception.LoginBlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(
            BadCredentialsException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Identifiants incorrects");
    }

    @ExceptionHandler(LoginBlockedException.class)
    public ResponseEntity<String> handleLoginBlocked(
            LoginBlockedException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Trop de tentatives. Réessayez ultérieurement.");
    }
}