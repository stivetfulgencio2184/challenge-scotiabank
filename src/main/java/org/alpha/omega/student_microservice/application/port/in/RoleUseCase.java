package org.alpha.omega.student_microservice.application.port.in;

import org.alpha.omega.student_microservice.domain.model.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleUseCase {

    Flux<Role> getAllRoles();
    Mono<Role> getRoleById(Integer id);
    Mono<Role> getRoleByName(String name);
    Mono<Role> createRole(Role role);
}
