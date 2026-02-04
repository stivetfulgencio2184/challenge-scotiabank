package org.alpha.omega.student_microservice.application.port.out;

import org.alpha.omega.student_microservice.domain.model.User;
import reactor.core.publisher.Mono;


public interface UserRoleRepositoryPort {

    Mono<Boolean> existsRelationship(Integer userId, Integer roleId);
    Mono<Void> assignRole(Integer userId, Integer roleId);
    Mono<User> findRolesByUser(Integer userId);
}
