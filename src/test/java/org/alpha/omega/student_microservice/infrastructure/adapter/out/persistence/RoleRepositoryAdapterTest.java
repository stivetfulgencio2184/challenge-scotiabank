package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.domain.model.Role;
//import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.RolePersistenceMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.util.PersistenceHelper.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * DataR2dbcTest is a slice test that:
 * 1. Start up a context of Spring limited to R2DBC.
 * 2. Configure automatically a DB embedded (H2 in my case).
 * 3. Execute the scripts of SQL initialization: schema.sql and data.sql.
 * 4. He does it once per ApplicationContext.
 * KEY: isn't by test method, is by context, i.e., once per each test class.
 *      For this reason, is necessary we're using a DB distinct by context, making use of the parameter:
 *      -${random.uuid} in the url property defined in the application.yml file of the test 'resources' directory.
 */
//@DataR2dbcTest
//@Import(value = {RoleRepositoryAdapter.class, RolePersistenceMapperImpl.class})
//class RoleRepositoryAdapterTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserRoleRepository userRoleRepository;
//
//    @Autowired
//    private RoleRepositoryAdapter adapter;
//
//    @BeforeEach
//    void setUp() {
//        StepVerifier.create(
//                this.userRoleRepository.deleteAll()
//                        .then(this.userRepository.deleteAll())
//                        .then(this.roleRepository.deleteAll())
//        ).verifyComplete();
//    }
//
//    @Test
//    void testSaveRole() {
//        //Given
//        Role admin = roleFactory("Administrator", "admin");
//
//        //When and Then
//        StepVerifier.create(this.adapter.save(admin))
//                .assertNext(role -> assertRole(admin.getName(), admin.getAbbreviation(), role))
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindById() {
//        //Given
//        RoleEntity admin = roleEntityFactory("Administrator", "admin");
//
//        //When and Then
//        StepVerifier.create(this.roleRepository.save(admin)
//                        .map(RoleEntity::id)
//                        .flatMap(this.adapter::findById))
//                .assertNext(role -> assertRole(admin.name(), admin.abbreviation(), role))
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindByIdEmpty() {
//        //Given
//        Integer nonExistentId = 100;
//
//        //When and Then
//        StepVerifier.create(this.adapter.findById(nonExistentId))
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindByName() {
//        //Given
//        RoleEntity admin = roleEntityFactory("Administrator", "admin");
//
//        //When and Then
//        StepVerifier.create(this.roleRepository.save(admin)
//                        .map(RoleEntity::name)
//                        .flatMap(this.adapter::findByName))
//                .assertNext(role -> assertRole(admin.name(), admin.abbreviation(), role))
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindByNameEmpty() {
//        //Given
//        String nonExistentName = "Vip";
//
//        //Given
//        StepVerifier.create(this.adapter.findByName(nonExistentName))
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindAll() {
//        //Given
//        RoleEntity admin = roleEntityFactory("Administrator", "admin");
//        RoleEntity dba = roleEntityFactory("Database Administrator", "dba");
//        List<RoleEntity> expectedRoles = List.of(admin, dba);
//
//
//        //When and Then
//        StepVerifier.create(this.roleRepository.saveAll(Flux.just(admin, dba))
//                        .thenMany(this.adapter.findAll())
//                        .collectList())
//                .assertNext(roles -> {
//                    assertEquals(expectedRoles.size(), roles.size());
//                    Set<String> names = roles.stream()
//                            .map(Role::getName)
//                            .collect(Collectors.toSet());
//                    assertTrue(names.contains(admin.name()));
//                    assertTrue(names.contains(dba.name()));
//                })
//                .verifyComplete();
//    }
//}
