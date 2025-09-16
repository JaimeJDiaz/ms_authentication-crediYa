package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.LogPort;
import co.com.pragma.model.user.gateways.PasswordEncoder;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.InvalidCredentialsException;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final LogPort log;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> saveUser(User user) {
        log.info(SAVING_USER);
        return Mono.just(user)
                .flatMap(userToValidate -> {
                    userValidator.validateUser(userToValidate);
                    log.debug(USER_VALIDATION_PASSED);
                       return Mono.just(userToValidate);
                })
                .flatMap(validatedUser ->
                        userRepository.findByEmail(validatedUser.getEmail())
                                .flatMap(existingUser -> {
                                    log.error(USER_ALREADY_EXISTS);
                                    return Mono.<User>error(new UserEmailAlreadyExistsException(validatedUser.getEmail()));
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    String encodedPassword = passwordEncoder.encode(validatedUser.getPassword());
                                    validatedUser.setPassword(encodedPassword);
                                    validatedUser.setRoleName("CUSTOMER");
                                    validatedUser.setRoleId(2L);
                                    return userRepository.saveUser(validatedUser)
                                            .doOnSuccess(savedUser -> log.info(USER_SAVED))
                                            .map(savedUser -> {
                                                savedUser.setPassword(null);
                                                return savedUser;
                                            });
                                }))
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
                        userRepository.findById(validatedUser.getId())
                                .flatMap(existingUser -> userRepository.update(validatedUser))
                                .switchIfEmpty(Mono.error(new UserNotFoundException("User with ID " + validatedUser.getId() + " not found")))
                );
    }

    public Mono<User> getUser(BigInteger id) {
        if (id == null) throw new ValidationException(List.of("Id is required"));
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ERROR_FETCHING_USER_BY_ID)));
    }

    public Mono<User> getUserByEmail(String email) {
        if (email == null || email.isBlank()) throw new ValidationException(List.of(EMAIL_REQUIRED));
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ERROR_FETCHING_USER_BY_EMAIL)));
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserByIdentification(String documentId) {
        if (documentId == null || documentId.isBlank()) throw new ValidationException(List.of("identification is required"));
        return userRepository.findByDocumentId(documentId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("ERROR_FETCHING_USER_BY_IDENTIFICATION")));
    }

    public Mono<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales invalidas")))
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas")))
                .flatMap(user ->
                        roleRepository.findById(user.getRoleId())
                                .map(role -> {
                                    user.setRoleName(role.getName());
                                    return user;
                                })
                );
    }

}
