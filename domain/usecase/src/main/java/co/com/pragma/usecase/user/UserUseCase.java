package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.LogPort;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.UserEmailAlreadyExistsException;
import co.com.pragma.usecase.user.exceptions.UserNotFoundException;
import co.com.pragma.usecase.user.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;
import static co.com.pragma.usecase.user.LogMessages.*;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository repository;
    private final UserValidator userValidator;
    private final LogPort log;

    public Mono<User> saveUser(User user) {
        log.info(SAVING_USER);
        return Mono.just(user)
                .flatMap(userToValidate -> {
                    userValidator.validateUser(userToValidate);
                    log.debug(USER_VALIDATION_PASSED);
                    return Mono.just(userToValidate);
                })
                .flatMap(validatedUser ->
                        repository.findByEmail(validatedUser.getEmail())
                                .flatMap(existingUser -> {
                                    log.error(USER_ALREADY_EXISTS);
                                    return Mono.<User>error(new UserEmailAlreadyExistsException(validatedUser.getEmail()));
                                })
                                .switchIfEmpty(Mono.defer(() -> repository.saveUser(validatedUser)
                                        .doOnSuccess(savedUser -> log.info(USER_SAVED))))
                );
    }

    public Mono<User> updateUser(User user) {
        return Mono.just(user)
                .flatMap(userToValidate -> {
                    if (userToValidate.getId() == null) {
                        return Mono.error(new ValidationException(List.of("Id is required")));
                    }
                    userValidator.validateUser(userToValidate);
                    return Mono.just(userToValidate);
                })
                .flatMap(validatedUser ->
                        repository.findById(validatedUser.getId())
                                .flatMap(existingUser -> repository.update(validatedUser))
                                .switchIfEmpty(Mono.error(new UserNotFoundException("User with ID " + validatedUser.getId() + " not found")))
                );
    }

    public Mono<User> getUser(BigInteger id) {
        if (id == null) throw new ValidationException(List.of("Id is required"));
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ERROR_FETCHING_USER_BY_ID)));
    }

    public Mono<User> getUserByEmail(String email) {
        if (email == null || email.isBlank()) throw new ValidationException(List.of(EMAIL_REQUIRED));
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ERROR_FETCHING_USER_BY_EMAIL)));
    }

    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    public Mono<User> getUserByIdentification(String documentId) {
        if (documentId == null || documentId.isBlank()) throw new ValidationException(List.of("identification is required"));
        return repository.findByDocumentId(documentId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("ERROR_FETCHING_USER_BY_IDENTIFICATION")));
    }
}
