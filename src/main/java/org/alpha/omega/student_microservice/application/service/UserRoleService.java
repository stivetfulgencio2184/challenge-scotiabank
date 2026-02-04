package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.in.UserRoleUseCase;
import org.alpha.omega.student_microservice.application.port.out.RoleRepositoryPort;
import org.alpha.omega.student_microservice.application.port.out.UserRepositoryPort;
import org.alpha.omega.student_microservice.application.port.out.UserRoleRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class UserRoleService implements UserRoleUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRoleRepositoryPort userRoleRepositoryPort;
    private final TransactionalOperator transactionalOperator;

    public UserRoleService(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort,
                           UserRoleRepositoryPort userRoleRepositoryPort, TransactionalOperator transactionalOperator) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
        this.userRoleRepositoryPort = userRoleRepositoryPort;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Void> assignRoleToUser(Integer userId, Integer roleId) {
        return this.transactionalOperator.execute(status ->
                this.roleRepositoryPort.findById(roleId)
                        .switchIfEmpty(Mono.error(new NotFoundException(
                                ApplicationMessages.Exceptions.NOT_FOUND.formatted(ApplicationMessages.ROLE,
                                        ApplicationMessages.ID, roleId)
                        )))
                        .flatMap(role -> this.userRepositoryPort.findById(userId))
                        .switchIfEmpty(Mono.error(new NotFoundException(
                                ApplicationMessages.Exceptions.NOT_FOUND.formatted(ApplicationMessages.USER,
                                        ApplicationMessages.ID, userId)
                        )))
                        .flatMap(user -> this.userRoleRepositoryPort.existsRelationship(userId, roleId))
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                                        ? Mono.error(new AlreadyRegisteredException(
                                        ApplicationMessages.Exceptions.ALREADY_REGISTERED.formatted(
                                                ApplicationMessages.RELATIONSHIP, ApplicationMessages.IDS,
                                                userId + ApplicationMessages.COMMA + roleId)))
                                        : this.userRoleRepositoryPort.assignRole(userId, roleId)
                        )
                ).next();
    }

    @Override
    public Mono<User> getRolesByUser(Integer userId) {
        return this.userRepositoryPort.findById(userId)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        ApplicationMessages.Exceptions.NOT_FOUND.formatted(ApplicationMessages.USER,
                                ApplicationMessages.ID, userId)
                )))
                .flatMap(user ->  this.userRoleRepositoryPort.findRolesByUser(userId));
    }
}
