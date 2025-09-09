package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Optional;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, BigInteger>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmail(String email);

    Mono<UserEntity> findByDocumentId(String documentId);
}
