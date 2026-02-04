package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.out.RoleRepositoryPort;
import org.alpha.omega.student_microservice.application.port.out.UserRepositoryPort;
import org.alpha.omega.student_microservice.application.port.out.UserRoleRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationTestMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.alpha.omega.student_microservice.application.util.ServiceHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class UserRoleServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private UserRoleRepositoryPort userRoleRepositoryPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private UserRoleService service;

    @Test
    void testRoleNotExists() {
        //Given
        given(this.roleRepositoryPort.findById(1)).willReturn(Mono.empty());
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.service.assignRoleToUser(1, 1))
                .expectErrorSatisfies(error ->
                        assertThat(error)
                            .isInstanceOf(NotFoundException.class)
                            .hasMessage(String.format(ApplicationTestMessages.Exceptions.NOT_FOUND,
                                    ApplicationTestMessages.ROLE, ApplicationTestMessages.ID, 1))
                ).verify();
        then(this.roleRepositoryPort).should(times(1)).findById(1);
        verifyNoMoreInteractions(this.userRepositoryPort, this.userRoleRepositoryPort);
    }

    @Test
    void testUserNotExists() {
        //Given
        given(this.roleRepositoryPort.findById(1)).willReturn(Mono.just(Role.builder().id(1).build()));
        given(this.userRepositoryPort.findById(1)).willReturn(Mono.empty());
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.service.assignRoleToUser(1,1))
                .expectErrorSatisfies(error ->
                        assertThat(error)
                            .isInstanceOf(NotFoundException.class)
                            .hasMessage(String.format(ApplicationTestMessages.Exceptions.NOT_FOUND,
                                    ApplicationTestMessages.USER, ApplicationTestMessages.ID, 1))
                ).verify();
        then(this.roleRepositoryPort).should(times(1)).findById(1);
        then(this.userRepositoryPort).should(times(1)).findById(1);
        verifyNoMoreInteractions(this.userRoleRepositoryPort);
    }

    @Test
    void testExistsRelationship() {
        //Given
        given(this.roleRepositoryPort.findById(1)).willReturn(Mono.just(Role.builder().id(1).build()));
        given(this.userRepositoryPort.findById(1)).willReturn(Mono.just(User.builder().id(1).build()));
        given(this.userRoleRepositoryPort.existsRelationship(1,1)).willReturn(Mono.just(true));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.service.assignRoleToUser(1,1))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(AlreadyRegisteredException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.ALREADY_REGISTERED, ApplicationTestMessages.RELATIONSHIP,
                                    ApplicationTestMessages.IDS, 1 + ApplicationTestMessages.COMMA + 1
                            ))
                ).verify();
        then(this.roleRepositoryPort).should(times(1)).findById(1);
        then(this.userRepositoryPort).should(times(1)).findById(1);
        then(this.userRoleRepositoryPort).should(times(1)).existsRelationship(1,1);
    }

    @Test
    void testAssignRoleIfRelationshipNotExists() {
        //Given
        given(this.roleRepositoryPort.findById(1)).willReturn(Mono.just(Role.builder().id(1).build()));
        given(this.userRepositoryPort.findById(1)).willReturn(Mono.just(User.builder().id(1).build()));
        given(this.userRoleRepositoryPort.existsRelationship(1,1)).willReturn(Mono.just(false));
        given(this.userRoleRepositoryPort.assignRole(1,1)).willReturn(Mono.empty());
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.service.assignRoleToUser(1,1))
                .expectNextCount(0)
                .verifyComplete();
        then(this.roleRepositoryPort).should(times(1)).findById(1);
        then(this.userRepositoryPort).should(times(1)).findById(1);
        then(this.userRoleRepositoryPort).should(times(1)).existsRelationship(1,1);
        then(this.userRoleRepositoryPort).should(times(1)).assignRole(1,1);
    }

    @Test
    void testGetRolesByUserNotExists() {
        //Given
        given(this.userRepositoryPort.findById(1)).willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.service.getRolesByUser(1))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(NotFoundException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.NOT_FOUND, ApplicationTestMessages.USER,
                                    ApplicationTestMessages.ID, 1
                            ))
                ).verify();
        then(this.userRepositoryPort).should(times(1)).findById(1);
    }

    @Test
    void testGetRolesByUser() {
        //Given
        User sfulgencio = userFactory(1, "sfulgencio", true,
                Set.of(roleFactory(1, "Administrator", "admin"),
                        roleFactory(2, "Database Administrator", "dba")));
        given(this.userRepositoryPort.findById(1)).willReturn(Mono.just(User.builder().id(1).build()));
        given(this.userRoleRepositoryPort.findRolesByUser(1)).willReturn(Mono.just(sfulgencio));

        //When and Then
        StepVerifier.create(this.service.getRolesByUser(1))
                .assertNext(user -> {
                    assertEquals(sfulgencio.getId(), user.getId());
                    assertEquals(sfulgencio.getUsername(), user.getUsername());
                    assertEquals(sfulgencio.getPassword(), user.getPassword());
                    assertEquals(sfulgencio.getEnabled(), user.getEnabled());
                    assertEquals(sfulgencio.getRoles().size(), user.getRoles().size());
                })
                .verifyComplete();
        then(this.userRepositoryPort).should(times(1)).findById(1);
        then(this.userRoleRepositoryPort).should(times(1)).findRolesByUser(1);
    }
}
