package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.in.UserUseCase;
import org.alpha.omega.student_microservice.application.port.out.UserRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort repositoryPort;
    private final TransactionalOperator transactionalOperator;

    public UserService(UserRepositoryPort repositoryPort, TransactionalOperator transactionalOperator) {
        this.repositoryPort = repositoryPort;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> getUserByUsername(String username) {
        return this.repositoryPort.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        String.format(ApplicationMessages.Exceptions.NOT_FOUND,
                                ApplicationMessages.USER, ApplicationMessages.USERNAME, username))));
    }

    @Override
    public Mono<User> createUser(User user) {
        return this.transactionalOperator.execute(status ->
                this.getUserByUsername(user.getUsername())
                        .flatMap(registeredUser ->
                                Mono.<User>error(new AlreadyRegisteredException(
                                        String.format(ApplicationMessages.Exceptions.ALREADY_REGISTERED,
                                                ApplicationMessages.USER, ApplicationMessages.USERNAME,
                                                registeredUser.getUsername()))))
                        .onErrorResume(NotFoundException.class,
                                ex -> this.repositoryPort.save(user))
        ).single();
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.repositoryPort.findAll();
    }
}
