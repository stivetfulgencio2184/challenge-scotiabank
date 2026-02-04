package org.alpha.omega.student_microservice.application.port.in;

import org.alpha.omega.student_microservice.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRoleUseCase {

    Mono<Void> assignRoleToUser(Integer userId, Integer roleId);
    Mono<User> getRolesByUser(Integer userId);
}
