package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.groupkey.RoleGroupKey;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.groupkey.UserGroupKey;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.projection.UserRoleProjection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Mapper(componentModel = "spring")
public interface UserRolePersistenceMapper {

    UserRolePersistenceMapper INSTANCE = Mappers.getMapper(UserRolePersistenceMapper.class);

    default Mono<User> toDomain(Flux<UserRoleProjection> rows) {
        return rows
                .groupBy(userRoleProjection -> new UserGroupKey(userRoleProjection.userId(),
                        userRoleProjection.userName(), userRoleProjection.enabled()))
                .flatMap(userGroup -> userGroup
                        .groupBy(userRoleProjection -> new RoleGroupKey(userRoleProjection.roleId(),
                                userRoleProjection.roleName(), userRoleProjection.abbreviation()))
                        .flatMap(roleGroup -> roleGroup
                                .filter(userRoleProjection -> userRoleProjection.roleId() != null)
                                .map(userRoleProjection -> Role.builder()
                                        .id(userRoleProjection.roleId())
                                        .name(userRoleProjection.roleName())
                                        .abbreviation(userRoleProjection.abbreviation())
                                        .build()))
                        .collectList()
                        .map(roles -> {
                            UserGroupKey userGroupKey = userGroup.key();
                            return User.builder()
                                    .id(userGroupKey.id())
                                    .username(userGroupKey.username())
                                    .enabled(userGroupKey.enabled())
                                    .roles(new HashSet<>(roles))
                                    .build();
                        }))
                .next();
    }
}
