package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository repository;
    private final UserValidator userValidator;

    //@Transactional
    public Mono<User> saveUser(User user) {
        userValidator.validateUser(user);

        return repository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.<User>error(new UserEmailAlreadyExistsException(user.getEmail())))
                .switchIfEmpty(Mono.defer(() -> repository.saveUser(user)));

    }


    //@Transactional
    public Mono<User> updateUser(User user) {
        userValidator.validateUser(user);
        return repository.update(user);
    }

    //@Transactional
    public Mono<Void> deleteUser(BigInteger id) {
        return repository.deleteById(id);
    }

    //@Transactional(readOnly = true)
    public Mono<User> getUser(BigInteger id) {
        return repository.findById(id);
    }

    //@Transactional(readOnly = true)
    public Mono<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    //@Transactional(readOnly = true)
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }
}
