package org.alpha.omega.student_microservice.application.port.in;

import org.alpha.omega.student_microservice.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserUseCase {

    Flux<User> getAllUsers();
    Mono<User> getUserByUsername(String username);
    Mono<User> createUser(User user);
}
