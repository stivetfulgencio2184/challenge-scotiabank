package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.application.port.out.UserRepositoryPort;
import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository repository;
    private final UserPersistenceMapper mapper;

    public UserRepositoryAdapter(UserRepository repository, UserPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return this.repository
                .save(UserPersistenceMapper.INSTANCE.toEntity(user))
                .map(this.mapper::toDomain);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return this.repository.findByUsername(username)
                .map(this.mapper::toDomain);
    }

    @Override
    public Mono<User> findById(Integer userId) {
        return this.repository.findById(userId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return this.repository.findAll()
                .map(this.mapper::toDomain);
    }
}
