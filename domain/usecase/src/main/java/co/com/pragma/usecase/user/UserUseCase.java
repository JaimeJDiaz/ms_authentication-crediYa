package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

import static co.com.pragma.usecase.user.LogMessages.*;
import static co.com.pragma.usecase.user.UserValidator.validateUser;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository repository;
    private static final Logger log = LoggerFactory.getLogger(UserUseCase.class);

    //@Transactional
    public Mono<User> saveUser(User user) {
        log.info(SAVING_USER, user.getEmail());
        validateUser(user);
        return repository.findByEmail(user.getEmail())
                .flatMap(existingUser -> {
                    log.warn(USER_ALREADY_EXISTS, user.getEmail());
                    return Mono.<User>error(new UserEmailAlreadyExistsException(user.getEmail()));
                })
                .switchIfEmpty(repository.saveUser(user)
                        .doOnSuccess(saved -> log.info(USER_SAVED))
                        .doOnError(e -> log.error(ERROR_SAVING_USER, e.getMessage(), e)));
    }

    //@Transactional
    public Mono<User> updateUser(User user) {
        log.info(UPDATING_USER);
        validateUser(user);
        return repository.update(user)
                .doOnSuccess(updated -> log.info(USER_UPDATED))
                .doOnError(e -> log.error(ERROR_UPDATING_USER, e.getMessage(), e));
    }

    //@Transactional
    public Mono<Void> deleteUser(BigInteger id) {
        log.info(DELETING_USER, id);
        return repository.deleteById(id)
                .doOnSuccess(v -> log.info(USER_DELETED, id))
                .doOnError(e -> log.error(ERROR_DELETING_USER, id, e.getMessage(), e));
    }

    //@Transactional(readOnly = true)
    public Mono<User> getUser(BigInteger id) {
        log.debug(FETCHING_USER_BY_ID, id);
        return repository.findById(id)
                .doOnNext(user -> log.info(USER_FOUND, id))
                .doOnError(e -> log.error(ERROR_FETCHING_USER_BY_ID, id, e.getMessage(), e));
    }

    //@Transactional(readOnly = true)
    public Mono<User> getUserByEmail(String email) {
        log.debug(FETCHING_USER_BY_EMAIL, email);
        return repository.findByEmail(email)
                .doOnNext(user -> log.info(USER_FOUND, user.getEmail()))
                .doOnError(e -> log.error(ERROR_FETCHING_USER_BY_EMAIL, email, e.getMessage(), e));
    }

    //@Transactional(readOnly = true)
    public Flux<User> getAllUsers() {
        log.debug(FETCHING_ALL_USERS);
        return repository.findAll()
                .doOnComplete(() -> log.info(ALL_USERS_FETCHED))
                .doOnError(e -> log.error(ERROR_FETCHING_ALL_USERS, e.getMessage(), e));
    }
}
