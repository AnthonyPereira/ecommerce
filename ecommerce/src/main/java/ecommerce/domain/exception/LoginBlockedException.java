package ecommerce.domain.exception;

public class LoginBlockedException extends RuntimeException {

    public LoginBlockedException() {
        super("Trop de tentatives de connexion. Réessayez ultérieurement.");
    }
}