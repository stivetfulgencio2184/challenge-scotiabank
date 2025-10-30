package org.alpha.omega.student_microservice.infrastructure.adapter.in.web;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebTestMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.dto.StudentDTOV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StudentRepository repository;

    @Value(value = "${api.version}")
    private String apiVersion;

    @Value(value = "${resource}")
    private String resource;

    @BeforeEach
    void setUp() {
        this.repository.deleteAll().thenMany(
                Flux.just(
                        new StudentEntity(null, 777, "Jesús", "Salvador", Boolean.TRUE, 33),
                        new StudentEntity(null, 246, "Stivet", "Fulgencio", Boolean.FALSE, 41),
                        new StudentEntity(null, 864, "Mary", "Ruiz", Boolean.TRUE, 30)
                ).flatMap(this.repository::save)
        ).blockLast();
    }

    @Test
    void testGetStudent() {
        this.webTestClient.get().uri(this.apiVersion + this.resource)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].id").isEqualTo(777)
                .jsonPath("$[0].name").isEqualTo("Jesús");
    }

    @Test
    void testFindActiveStudents() {
        this.webTestClient.get().uri(this.apiVersion + this.resource + "/{status}", Boolean.TRUE)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(777)
                .jsonPath("$[0].name").isEqualTo("Jesús")
                .jsonPath("$[1].id").isEqualTo(864)
                .jsonPath("$[1].name").isEqualTo("Mary");
    }

    @Test
    void testCreateStudentAlreadyRegistered() {
        StudentDTOV1 newStudent = new StudentDTOV1(777, "Jesús", "Salvador", Boolean.TRUE, 33);

        this.webTestClient.post().uri(this.apiVersion + this.resource)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.message").isEqualTo(String.format(
                        WebTestMessages.Exceptions.ALREADY_REGISTERED, newStudent.id()));
    }

    @Test
    void testCreateStudentBadRequest() {
        StudentDTOV1 newStudent = new StudentDTOV1(null, "Marcos", "Evi", Boolean.TRUE, 24);

        this.webTestClient.post().uri(this.apiVersion + this.resource)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo(
                        String.format(WebTestMessages.Exceptions.REQUIRED_FILE, WebTestMessages.StudentDTOV1Field.ID));
    }

    @Test
    void testCreateStudent() {
        StudentDTOV1 newStudent = new StudentDTOV1(987, "Marcos", "Evi", Boolean.TRUE, 24);

        this.webTestClient.post().uri(this.apiVersion + this.resource)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(newStudent.id())
                .jsonPath("$.name").isEqualTo(newStudent.name())
                .jsonPath("$.lastName").isEqualTo(newStudent.lastName())
                .jsonPath("$.status").isEqualTo(newStudent.status())
                .jsonPath("$.age").isEqualTo(newStudent.age());
        StepVerifier.create(this.repository.findAll().count())
                .expectNext(4L)
                .verifyComplete();
    }
}
