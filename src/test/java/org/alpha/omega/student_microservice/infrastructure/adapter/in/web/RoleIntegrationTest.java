package org.alpha.omega.student_microservice.infrastructure.adapter.in.web;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder.RoleIntegrationDataBuilder;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebTestMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.RoleIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.RoleRequestV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

//@SpringBootTest
//@AutoConfigureWebTestClient
//@Tag(value = "integration")
//class RoleIntegrationTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private RoleIntegrationDataBuilder roleIntegrationDataBuilder;
//
//    @Value(value = "${api.version}")
//    private String apiVersion;
//
//    @Value(value = "${resource.roles}")
//    private String roleResource;
//
//    private RoleIntegrationData roleData;
//
//    @BeforeEach
//    void setUp() {
//        StepVerifier.create(this.roleIntegrationDataBuilder.build())
//                .assertNext(roleIntegrationData -> this.roleData = roleIntegrationData)
//                .verifyComplete();
//    }
//
//    @Test
//    void testGetAllRoles() {
//        this.webTestClient.get().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.GET_ALL.formatted(WebTestMessages.ROLE))
//                .jsonPath("$.data").isArray()
//                .jsonPath("$.data[?(@.abbreviation=='admin')].name").value(hasItem("Administrator"))
//                .jsonPath("$.data[?(@.abbreviation=='dba')].name").value(hasItem("Database Administrator"))
//                .jsonPath("$.data[?(@.abbreviation=='usr')].name").value(hasItem("User"));
//    }
//
//    @Test
//    void testNotFoundRoleByName() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .queryParam("name", "Editor")
//                        .build())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND.formatted(
//                        WebTestMessages.ROLE, WebTestMessages.RoleField.NAME, "Editor"
//                ));
//    }
//
//    @Test
//    void testFoundRoleByName() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .queryParam("name", "Administrator")
//                        .build())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.ROLE))
//                .jsonPath("$.data.name").isEqualTo("Administrator")
//                .jsonPath("$.data.abbreviation").isEqualTo("admin");
//    }
//
//    @Test
//    void testFindRoleByIdBadRequest() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .build("1d"))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(false)
//                .jsonPath("$.code").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.INVALID_ID
//                        .formatted(WebTestMessages.ROLE));
//    }
//
//    @Test
//    void testNotFoundRoleById() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .build(this.roleData.admin() + 10))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND
//                        .formatted(WebTestMessages.ROLE, WebTestMessages.RoleField.ID, this.roleData.admin() + 10));
//    }
//
//    @Test
//    void testFindRoleById() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .build(this.roleData.admin()))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.ROLE))
//                .jsonPath("$.data.id").isEqualTo(this.roleData.admin())
//                .jsonPath("$.data.name").isEqualTo("Administrator")
//                .jsonPath("$.data.abbreviation").isEqualTo("admin");
//    }
//
//    @Test
//    void testCreateRoleWithNameNull() {
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(new RoleRequestV1(null, "mgr"))
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Validation.Role.NAME_NOT_BLANK);
//    }
//
//    @Test
//    void testCreateRoleWithAbbreviationNull() {
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(new RoleRequestV1("Manager", null))
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Validation.Role.ABBREVIATION_NOT_BLANK);
//    }
//
//    @Test
//    void testCreateRoleAlreadyRegistered() {
//        RoleRequestV1 roleToCreate = new RoleRequestV1("Administrator", "admin");
//
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(roleToCreate)
//                .exchange()
//                .expectStatus().isEqualTo(CONFLICT.value())
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(CONFLICT.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.ALREADY_REGISTERED
//                        .formatted(WebTestMessages.ROLE, WebTestMessages.RoleField.NAME, roleToCreate.name()));
//    }
//
//    @Test
//    void testCreateRole() {
//        RoleRequestV1 roleToCreate = new RoleRequestV1("Manager", "mgr");
//
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(roleToCreate)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(CREATED.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.REGISTER_SUCCESSFULLY
//                        .formatted(WebTestMessages.ROLE))
//                .jsonPath("$.data.id").isNotEmpty()
//                .jsonPath("$.data.name").isEqualTo(roleToCreate.name())
//                .jsonPath("$.data.abbreviation").isEqualTo(roleToCreate.abbreviation());
//    }
//
//    private String getApiVersion() {
//        return this.apiVersion + this.roleResource;
//    }
//}
