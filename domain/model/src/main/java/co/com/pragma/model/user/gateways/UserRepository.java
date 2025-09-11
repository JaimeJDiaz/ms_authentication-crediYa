package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserRepository {
    Mono<User> saveUser(User user);

    Flux<User> findAll();

    Mono<User> findByEmail(String email);

    Mono<User> findById(BigInteger id);

    Mono<User> update(User user);

    Mono<Void> deleteById(BigInteger id);
    Mono<User> findByDocumentId(String documentId);
}
