package co.com.pragma.api.exceptionhandler;


import co.com.pragma.usecase.user.exceptions.InvalidCredentialsException;
import co.com.pragma.usecase.user.exceptions.UserEmailAlreadyExistsException;
import co.com.pragma.usecase.user.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", ex.getErrors());

        return Mono.just(ResponseEntity.badRequest().body(body));
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public Mono<ResponseEntity<String>> handleUserEmailAlreadyExists(UserEmailAlreadyExistsException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ex.getMessage())
        );
    }
}
