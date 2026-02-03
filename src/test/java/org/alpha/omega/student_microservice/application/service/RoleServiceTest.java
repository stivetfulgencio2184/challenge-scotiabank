package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.out.RoleRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationTestMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.domain.model.Role;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(value = MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepositoryPort repositoryPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testGetRoleByIdNotFound() {
        //Given
        given(this.repositoryPort.findById(1))
                .willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.roleService.getRoleById(1))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(NotFoundException.class)
                            .hasMessage(String.format(ApplicationTestMessages.Exceptions.NOT_FOUND,
                                    ApplicationTestMessages.ROLE, ApplicationTestMessages.ID, 1))
                ).verify();
        then(this.repositoryPort).should(times(1)).findById(1);
    }

    @Test
    void testGetRoleById() {
        //Given
        Role admin = roleFactory(1, "Administrator", "Admin");
        given(this.repositoryPort.findById(1))
                .willReturn(Mono.just(admin));

        //When and Then
        StepVerifier.create(this.roleService.getRoleById(1))
                .assertNext(role -> assertRole(admin, role))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findById(1);
    }

    @Test
    void testGetRoleByNameNotFound() {
        //Given
        given(this.repositoryPort.findByName("admin"))
                .willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.roleService.getRoleByName("admin"))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(NotFoundException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.NOT_FOUND, ApplicationTestMessages.ROLE,
                                    ApplicationTestMessages.NAME, "admin"
                            ))).verify();
        then(this.repositoryPort).should(times(1)).findByName("admin");
    }

    @Test
    void testGetRoleByName() {
        //Given
        Role admin = roleFactory(1, "Administrator", "admin");
        given(this.repositoryPort.findByName(admin.getName()))
                .willReturn(Mono.just(admin));

        //When and Then
        StepVerifier.create(this.roleService.getRoleByName(admin.getName()))
                .assertNext(role -> assertRole(admin, role))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByName(admin.getName());
    }

    @Test
    void testCreateRoleAlreadyRegistered() {
        //Given
        Role roleToSaved = roleFactory(1, "Administrator", "admin");
        given(this.repositoryPort.findByName(roleToSaved.getName()))
                .willReturn(Mono.just(roleToSaved));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.roleService.createRole(roleToSaved))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(AlreadyRegisteredException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.ALREADY_REGISTERED, ApplicationTestMessages.ROLE,
                                    ApplicationTestMessages.NAME, roleToSaved.getName()
                            ))).verify();
        then(this.repositoryPort).should(times(1)).findByName(roleToSaved.getName());
        then(this.repositoryPort).should(never()).save(any());
    }

    @Test
    void testCreateRole() {
        //Given
        Role roleToSaved =roleFactory(null, "Administrator", "admin");
        Role savedRole = roleFactory(1, "Administrator", "admin");
        given(this.repositoryPort.findByName(roleToSaved.getName()))
                .willReturn(Mono.empty());
        given(this.repositoryPort.save(roleToSaved))
                .willReturn(Mono.just(savedRole));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.roleService.createRole(roleToSaved))
                .assertNext(role -> assertRole(roleToSaved, role))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByName(roleToSaved.getName());
        then(this.repositoryPort).should(times(1)).save(roleToSaved);
    }

    @Test
    void testGetAllRoles() {
        //Given
        Role adminRole = roleFactory(1, "Administrator", "admin");
        Role dbaRole = roleFactory(2, "Database Administrator", "dba");
        List<Role> expectedRoles = List.of(adminRole, dbaRole);
        given(this.repositoryPort.findAll())
                .willReturn(Flux.just(adminRole, dbaRole));

        //When and Then
        StepVerifier.create(this.roleService.getAllRoles()
                        .collectList())
                .assertNext(roles -> {
                    assertEquals(expectedRoles.size(), roles.size());
                    Set<String> names = roles.stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());
                    assertTrue(names.contains(adminRole.getName()));
                    assertTrue(names.contains(dbaRole.getName()));
                })
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findAll();
    }
}
