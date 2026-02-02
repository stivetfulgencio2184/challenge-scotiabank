package org.alpha.omega.student_microservice.infrastructure.adapter.in.web;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder.StudentIntegrationDataBuilder;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebTestMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.StudentRequestV1;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static java.lang.Boolean.TRUE;
import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.*;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
@Tag(value = "integration")
class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StudentIntegrationDataBuilder studentIntegrationDataBuilder;

    @Value(value = "${api.version}")
    private String apiVersion;

    @Value(value = "${resource.students}")
    private String resource;

    @BeforeEach
    void setUp() {
        StepVerifier.create(
                this.studentIntegrationDataBuilder.build()
        ).verifyComplete();
    }

    @Test
    void testGetStudent() {
        this.webTestClient.get().uri(getBaseUri())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flag").isEqualTo(TRUE)
                .jsonPath("$.code").isEqualTo(OK.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.GET_ALL.formatted(WebTestMessages.STUDENT))
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[?(@.id==777)].name").value(hasItem("Jesús"))
                .jsonPath("$.data[?(@.id==246)].name").value(hasItem("Stivet"))
                .jsonPath("$.data[?(@.id==864)].name").value(hasItem("Mary"));
    }

    @Test
    void testUndefinedStudentStatus() {
        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getBaseUri())
                        .queryParam("status", "down")
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Validation.Student.STATUS_PARAMETER);
    }

    @Test
    void testFindActiveStudents() {
        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getBaseUri())
                        .queryParam("status", "true")
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flag").isEqualTo(TRUE)
                .jsonPath("$.code").isEqualTo(OK.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.STUDENT))
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[?(@.id==777)].id").value(hasItem(777))
                .jsonPath("$.data[?(@.id==777)].name").value(hasItem("Jesús"))
                .jsonPath("$.data[?(@.id==864)].id").value(hasItem(864))
                .jsonPath("$.data[?(@.id==864)].name").value(hasItem("Mary"));
    }

    @Test
    void testFindInactiveStudents() {
        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getBaseUri())
                        .queryParam("status", "false")
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flag").isEqualTo(TRUE)
                .jsonPath("$.code").isEqualTo(OK.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.STUDENT))
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[?(@.id==246)].id").value(hasItem(246))
                .jsonPath("$.data[?(@.id==246)].name").value(hasItem("Stivet"));
    }

    @Test
    void testCreateStudentAlreadyRegistered() {
        StudentRequestV1 newStudent = WebFactory.studentDTOV1Factory(777, "Jesús", "Salvador", TRUE, 33);

        this.webTestClient.post().uri(getBaseUri())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody()
                .jsonPath("$.status").isEqualTo(CONFLICT.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.ALREADY_REGISTERED
                        .formatted(WebTestMessages.STUDENT, WebTestMessages.StudentDTOV1Field.ID, newStudent.id()));
    }

    @Test
    void testCreateStudentBadRequest() {
        StudentRequestV1 newStudent = studentDTOV1Factory(null, "Marcos", "Evi", TRUE, 24);

        this.webTestClient.post().uri(getBaseUri())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.REQUIRED_FILE
                        .formatted(WebTestMessages.STUDENT, WebTestMessages.StudentDTOV1Field.ID));
    }

    @Test
    void testCreateStudent() {
        StudentRequestV1 newStudent = studentDTOV1Factory(987, "Marcos", "Evi", TRUE, 24);

        this.webTestClient.post().uri(getBaseUri())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.flag").isEqualTo(TRUE)
                .jsonPath("$.code").isEqualTo(CREATED.value())
                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.REGISTER_SUCCESSFULLY
                        .formatted(WebTestMessages.STUDENT))
                .jsonPath("$.data.id").isEqualTo(newStudent.id())
                .jsonPath("$.data.name").isEqualTo(newStudent.name())
                .jsonPath("$.data.lastName").isEqualTo(newStudent.lastName())
                .jsonPath("$.data.status").isEqualTo(newStudent.status())
                .jsonPath("$.data.age").isEqualTo(newStudent.age());
    }

    private String getBaseUri() {
        return this.apiVersion + this.resource;
    }
}
