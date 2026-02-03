package org.alpha.omega.student_microservice.application.port.out;

import org.alpha.omega.student_microservice.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {

    Mono<User> findByUsername(String username);
    Mono<User> findById(Integer userId);
    Mono<User> save(User user);
    Flux<User> findAll();
}
