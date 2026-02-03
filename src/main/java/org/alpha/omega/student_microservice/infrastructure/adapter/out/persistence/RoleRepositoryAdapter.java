package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.application.port.out.RoleRepositoryPort;
import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.RolePersistenceMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleRepository repository;
    private final RolePersistenceMapper mapper;

    public RoleRepositoryAdapter(RoleRepository repository, RolePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Role> save(Role role) {
        return this.repository
                .save(RolePersistenceMapper.INSTANCE.toEntity(role))
                .map(this.mapper::toDomain);
    }

    @Override
    public Mono<Role> findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::toDomain);
    }

    @Override
    public Mono<Role> findByName(String name) {
        return this.repository.findByName(name)
                .map(this.mapper::toDomain);
    }

    @Override
    public Flux<Role> findAll() {
        return this.repository.findAll()
                .map(this.mapper::toDomain);
    }
}
