package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.in.RoleUseCase;
import org.alpha.omega.student_microservice.application.port.out.RoleRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoleService implements RoleUseCase {

    private final RoleRepositoryPort repositoryPort;
    private final TransactionalOperator transactionalOperator;

    public RoleService(RoleRepositoryPort repositoryPort, TransactionalOperator transactionalOperator) {
        this.repositoryPort = repositoryPort;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Role> getRoleById(Integer id) {
        return this.repositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        ApplicationMessages.Exceptions.NOT_FOUND.formatted(
                                ApplicationMessages.ROLE, ApplicationMessages.ID, id))));
    }

    @Override
    public Mono<Role> getRoleByName(String name) {
        return this.repositoryPort.findByName(name)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        ApplicationMessages.Exceptions.NOT_FOUND.formatted(
                                ApplicationMessages.ROLE, ApplicationMessages.NAME, name)
                )));
    }

    @Override
    public Mono<Role> createRole(Role role) {
        return this.transactionalOperator.execute(status ->
                this.getRoleByName(role.getName())
                        .flatMap(registeredRole ->
                                Mono.<Role>error(new AlreadyRegisteredException(
                                        ApplicationMessages.Exceptions.ALREADY_REGISTERED.formatted(
                                                ApplicationMessages.ROLE, ApplicationMessages.NAME,
                                                registeredRole.getName())))
                        )
                        .onErrorResume(NotFoundException.class,
                                ex -> this.repositoryPort.save(role))
        ).single();
    }

    @Override
    public Flux<Role> getAllRoles() {
        return this.repositoryPort.findAll();
    }
}
