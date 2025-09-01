package co.com.pragma.usecase.user.exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
