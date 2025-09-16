package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.user.Role;
import co.com.pragma.r2dbc.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoleMapper implements Function<RoleEntity, Role> {
    @Override
    public Role apply(RoleEntity entity) {
        return new Role(entity.getId(), entity.getName(), entity.getDescription());
    }
}
