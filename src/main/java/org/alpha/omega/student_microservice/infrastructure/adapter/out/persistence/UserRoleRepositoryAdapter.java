package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.application.port.out.UserRoleRepositoryPort;
import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.UserRolePersistenceMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRoleRepositoryAdapter implements UserRoleRepositoryPort {

    private final UserRoleRepository userRoleRepository;

    public UserRoleRepositoryAdapter(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Mono<Boolean> existsRelationship(Integer userId, Integer roleId) {
        return this.userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .map(relationship -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> assignRole(Integer userId, Integer roleId) {
        return this.userRoleRepository.save(new UserRoleEntity(userId, roleId)).then();
    }

    @Override
    public Mono<User> findRolesByUser(Integer userId) {
        return UserRolePersistenceMapper.INSTANCE.toDomain(this.userRoleRepository.findRolesByUserId(userId));
    }
}
