package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends R2dbcRepository<RoleEntity, Integer> {

    Mono<RoleEntity> findByName(String name);
}
