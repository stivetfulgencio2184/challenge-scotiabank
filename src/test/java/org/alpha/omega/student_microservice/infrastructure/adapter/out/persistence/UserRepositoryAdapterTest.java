package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.UserPersistenceMapperImpl;
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

@DataR2dbcTest
@Import(value = {UserRepositoryAdapter.class, UserPersistenceMapperImpl.class})
class UserRepositoryAdapterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        StepVerifier.create(
                this.userRoleRepository.deleteAll()
                        .then(this.userRepository.deleteAll())
                        .then(this.roleRepository.deleteAll())
        ).verifyComplete();
    }

    @Test
    void testSaveUser() {
        //Given
        User jsalvador = userFactory("jesús", "$$Jesus2100$$", true, null);

        //When and Then
        StepVerifier.create(this.adapter.save(jsalvador))
                .assertNext(user -> assertUser(jsalvador.getUsername(), jsalvador.getPassword(), user))
                .verifyComplete();
    }

    @Test
    void testFindByUsername() {
        //Given
        UserEntity jsalvador = userEntityFactory(true, "jesús", "$$Jesus2100$$");

        //When and Them
        StepVerifier.create(this.userRepository.save(jsalvador)
                        .map(UserEntity::username)
                        .flatMap(this.adapter::findByUsername))
                .assertNext(user -> assertUser(jsalvador.username(), jsalvador.password(), user))
                .verifyComplete();
    }

    @Test
    void testFindByUsernameEmpty() {
        //Given
        String nonExistentUsername = "afulgencio";

        //When and Then
        StepVerifier.create(this.adapter.findByUsername(nonExistentUsername))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        //Given
        UserEntity jsalvador = userEntityFactory(true, "jesús", "$$Jesus2100$$");

        //When and Then
        StepVerifier.create(this.userRepository.save(jsalvador)
                        .map(UserEntity::id)
                        .flatMap(this.adapter::findById))
                .assertNext(user -> assertUser(jsalvador.username(), jsalvador.password(), user))
                .verifyComplete();
    }

    @Test
    void testFindByIdEmpty() {
        //Given
        Integer nonExistentId = 100;

        //When and Then
        StepVerifier.create(this.adapter.findById(nonExistentId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        //Given
        UserEntity jsalvador = userEntityFactory(true, "jesús", "$$Jesus2100$$");
        UserEntity sfulgencio = userEntityFactory(true, "sfulgencio", "$$Stiver2184$$");
        List<UserEntity> expectedUsers = List.of(jsalvador, sfulgencio);

        //When and Then
        StepVerifier.create(this.userRepository.saveAll(Flux.just(jsalvador, sfulgencio))
                        .thenMany(this.adapter.findAll())
                        .collectList())
                .assertNext(users -> {
                    assertEquals(expectedUsers.size(), users.size());
                    Set<String> names = users.stream()
                            .map(User::getUsername)
                            .collect(Collectors.toSet());
                    assertTrue(names.contains(jsalvador.username()));
                    assertTrue(names.contains(sfulgencio.username()));
                })
                .verifyComplete();
    }
}
