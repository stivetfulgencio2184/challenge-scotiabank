package org.alpha.omega.student_microservice.application.service;


import org.alpha.omega.student_microservice.application.port.out.UserRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationTestMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.alpha.omega.student_microservice.application.util.ServiceHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort repositoryPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserByUsernameNotFound() {
        //Given
        given(this.repositoryPort.findByUsername("stivet"))
                .willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.userService.getUserByUsername("stivet"))
                .expectErrorSatisfies(error -> assertThat(error)
                        .isInstanceOf(NotFoundException.class)
                        .hasMessage(String.format(
                                ApplicationTestMessages.Exceptions.NOT_FOUND, ApplicationTestMessages.USER,
                                ApplicationTestMessages.USERNAME, "stivet"))
                ).verify();
        then(this.repositoryPort).should(times(1)).findByUsername("stivet");
    }

    @Test
    void testGetUserByUsername() {
        //Given
        User sfulgencio = userFactory(1, "stivet", true, null);
        given(this.repositoryPort.findByUsername(sfulgencio.getUsername()))
                .willReturn(Mono.just(sfulgencio));

        //When and Then
        StepVerifier.create(this.userService.getUserByUsername(sfulgencio.getUsername()))
                .assertNext(user -> assertUser(sfulgencio, user))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByUsername(sfulgencio.getUsername());
    }

    @Test
    void testCreateUserAlreadyRegistered() {
        //Given
        User registeredUser = userFactory(1, "stivet", true, null);
        User newUser = userFactory(null, "stivet", true, null);
        given(this.repositoryPort.findByUsername(newUser.getUsername()))
                .willReturn(Mono.just(registeredUser));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.userService.createUser(newUser))
                .expectErrorSatisfies(error -> assertThat(error)
                            .isInstanceOf(AlreadyRegisteredException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.ALREADY_REGISTERED, ApplicationTestMessages.USER,
                                    ApplicationTestMessages.USERNAME, newUser.getUsername()
                            ))
                ).verify();
        then(this.repositoryPort).should(times(1)).findByUsername(newUser.getUsername());
    }

    @Test
    void testCreateUser() {
        //Given
        User registeredUser = userFactory(1, "stivet", true, null);
        User newUser = userFactory(null, "stivet", true, null);
        given(this.repositoryPort.findByUsername(newUser.getUsername()))
                .willReturn(Mono.empty());
        given(this.repositoryPort.save(newUser))
                .willReturn(Mono.just(registeredUser));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.userService.createUser(newUser))
                .assertNext(user -> assertUser(newUser, user))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByUsername(newUser.getUsername());
        then(this.repositoryPort).should(times(1)).save(newUser);
    }

    @Test
    void testGetAllUsers() {
        //Given
        User sfulgencio = userFactory(1, "stivet", true, null);
        User mruiz = userFactory(2, "mary", true, null);
        List<User> expectedUsers = List.of(sfulgencio, mruiz);
        given(this.repositoryPort.findAll()).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.userService.getAllUsers()
                        .collectList())
                .assertNext(users -> {
                    assertEquals(expectedUsers.size(), users.size());
                    Set<String> usernames = users.stream()
                            .map(User::getUsername)
                            .collect(Collectors.toSet());
                    assertTrue(usernames.contains(sfulgencio.getUsername()));
                    assertTrue(usernames.contains(mruiz.getUsername()));
                })
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findAll();
    }
}
