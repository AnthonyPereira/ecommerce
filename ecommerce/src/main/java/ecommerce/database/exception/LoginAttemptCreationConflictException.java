package ecommerce.database.exception;

public class LoginAttemptCreationConflictException extends RuntimeException {

    public LoginAttemptCreationConflictException(Throwable cause) {
        super(cause);
    }
}