package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.projection.UserRoleProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRoleRepository extends R2dbcRepository<UserRoleEntity, Void> {

    Mono<UserRoleEntity> findByUserIdAndRoleId(Integer userId, Integer roleId);

    @Query("""
            SELECT u.id AS user_id, u.enabled, u.username AS user_name,
                r.id AS role_id, r.name AS role_name, r.abbreviation
            FROM users u
            LEFT JOIN user_roles ur ON u.id = ur.user_id
            LEFT JOIN roles r ON ur.role_id = r.id
            WHERE u.id = :userId
            """)
    Flux<UserRoleProjection> findRolesByUserId(Integer userId);
}
