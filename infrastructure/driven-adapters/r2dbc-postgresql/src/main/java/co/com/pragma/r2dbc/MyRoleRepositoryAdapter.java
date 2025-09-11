package co.com.pragma.r2dbc;

import co.com.pragma.model.user.Role;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Repository
public class MyRoleRepositoryAdapter extends ReactiveAdapterOperations<Role, RoleEntity, Long, MyRoleRepository> implements RoleRepository {

    protected MyRoleRepositoryAdapter(MyRoleRepository repository, ObjectMapper mapper, Function<RoleEntity, Role> toEntityFn) {
        super(repository, mapper, entity -> mapper.map(entity, Role.class));
    }

    @Override
    public Mono<Role> findById(Long id) {
        return super.findById(id);
    }


}
