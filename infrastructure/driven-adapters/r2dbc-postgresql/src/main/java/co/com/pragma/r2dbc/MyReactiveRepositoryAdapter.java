package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, BigInteger, MyReactiveRepository> implements UserRepository{
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {

        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return super.repository.findByEmail(email)
                .map(entity -> super.mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> update(User user) {
        return super.repository.save(super.mapper.map(user, UserEntity.class))
                .map(entity -> super.mapper.map(entity, User.class));
    }

    @Override
    public Mono<Void> deleteById(BigInteger id) {
        return super.repository.deleteById(id);
    }

    @Override
    public Mono<User> findByIdentification(String identification) {
        return super.repository.findByIdentification(identification)
                .map(entity -> super.mapper.map(entity, User.class));
    }

    @Override
    public Flux<User> findAll() {
        return super.repository.findAll()
                .map(entity -> super.mapper.map(entity, User.class));
    }
}
