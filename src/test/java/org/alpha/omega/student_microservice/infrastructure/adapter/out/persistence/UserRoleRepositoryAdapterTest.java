package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.UserRolePersistenceMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.util.PersistenceHelper.roleEntityFactory;
import static org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.util.PersistenceHelper.userEntityFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Import(value = {UserRoleRepositoryAdapter.class, UserRolePersistenceMapperImpl.class})
class UserRoleRepositoryAdapterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        StepVerifier.create(
                this.userRoleRepository.deleteAll()
                        .then(this.userRepository.deleteAll())
                        .then(this.roleRepository.deleteAll())
        ).verifyComplete();
    }

    @Test
    void testNotExistsRelationship() {
        //Given
        Integer nonExistentUserId = 100;
        Integer nonExistentRoleId = 100;

        //When and Then
        StepVerifier.create(this.adapter.existsRelationship(nonExistentUserId, nonExistentRoleId))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void testExistsRelationship() {
        UserEntity jsalvador = userEntityFactory(true, "jsalvador", "$#Jesus0000#$");
        RoleEntity admin = roleEntityFactory("Administrator", "admin");

        StepVerifier.create(Mono.zip(
                                this.userRepository.save(jsalvador),
                                this.roleRepository.save(admin))
                        .flatMap(tuple -> {
                            UserEntity jsalvadorEntity = tuple.getT1();
                            RoleEntity adminEntity = tuple.getT2();
                            return this.userRoleRepository.save(new UserRoleEntity(jsalvadorEntity.id(), adminEntity.id()))
                                    .then(this.adapter.existsRelationship(jsalvadorEntity.id(), adminEntity.id()));
                        })
                ).expectNext(true)
                .verifyComplete();
    }

    @Test
    void testAssignRole() {
        //Given
        UserEntity jsalvador = userEntityFactory(true, "jsalvador", "$#Jesus0000#$");
        RoleEntity admin = roleEntityFactory("Administrator", "admin");

        //When and Then
        StepVerifier.create(Mono.zip(
                this.userRepository.save(jsalvador),
                this.roleRepository.save(admin)
        ).flatMap(tuple -> {
                        UserEntity jsalvadorEntity = tuple.getT1();
                        RoleEntity adminEntity = tuple.getT2();
                        return this.adapter.assignRole(jsalvadorEntity.id(), adminEntity.id())
                                .then(this.adapter.existsRelationship(jsalvadorEntity.id(), adminEntity.id()));
                    })
                )
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindRolesByUser() {
        //Given
        UserEntity jSalvador = userEntityFactory(true, "jsalvador", "$#Jesus0000#$");
        RoleEntity admin = roleEntityFactory("Administrator", "admin");
        RoleEntity dba = roleEntityFactory("Database Administrator", "dba");
        List<RoleEntity> expectedRoles = List.of(admin, dba);

        //When and Then
        StepVerifier.create(Mono.zip(
                this.userRepository.save(jSalvador),
                this.roleRepository.save(admin),
                this.roleRepository.save(dba)
                        ).flatMap(tuple -> {
                           UserEntity jSalvadorEntity = tuple.getT1();
                           RoleEntity adminEntity = tuple.getT2();
                           RoleEntity dbaEntity = tuple.getT3();
                           return this.userRoleRepository.save(new UserRoleEntity(jSalvadorEntity.id(), adminEntity.id()))
                                   .then(this.userRoleRepository.save(new UserRoleEntity(jSalvadorEntity.id(), dbaEntity.id())))
                                   .then(this.adapter.findRolesByUser(jSalvadorEntity.id()));
                        })
                ).assertNext(foundUser -> {
                    assertThat(foundUser.getId()).isPositive();
                    assertEquals(jSalvador.username(), foundUser.getUsername());
                    assertEquals(jSalvador.enabled(), foundUser.getEnabled());
                    assertNotNull(foundUser.getRoles());
                    assertEquals(expectedRoles.size(), foundUser.getRoles().size());

                    Set<String> roleNames = foundUser.getRoles()
                            .stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());

                    assertTrue(roleNames.contains(admin.name()));
                    assertTrue(roleNames.contains(dba.name()));
                })
                .verifyComplete();
    }

    @Test
    void testUserWithoutRoles() {
        //Given
        UserEntity jSalvador = userEntityFactory(true, "jsalvador", "$#Jesus0000#$");

        //When and Then
        StepVerifier.create(this.userRepository.save(jSalvador)
                .flatMap(jSalvadorEntity -> this.adapter.findRolesByUser(jSalvadorEntity.id())))
                .assertNext(foundUser -> {
                    assertEquals(jSalvador.username(), foundUser.getUsername());
                    assertEquals(jSalvador.enabled(), foundUser.getEnabled());
                    assertNotNull(foundUser.getRoles());
                    assertTrue(foundUser.getRoles().isEmpty());
                })
                .verifyComplete();
    }
}
