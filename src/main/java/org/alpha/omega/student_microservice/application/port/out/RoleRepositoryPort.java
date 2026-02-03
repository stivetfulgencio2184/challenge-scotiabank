package org.alpha.omega.student_microservice.application.port.out;

import org.alpha.omega.student_microservice.domain.model.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepositoryPort {

    Mono<Role> findById(Integer id);
    Mono<Role> findByName(String name);
    Mono<Role> save(Role role);
    Flux<Role> findAll();
}
