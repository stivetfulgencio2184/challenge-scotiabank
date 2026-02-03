package org.alpha.omega.student_microservice.infrastructure.adapter.in.web;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder.UserRoleIntegrationDataBuilder;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebTestMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.RoleIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.UserIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRequestV1;
//import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRoleRequestV1;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.*;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

//@SpringBootTest
//@AutoConfigureWebTestClient
//@Tag(value = "integration")
//class UserIntegrationTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private UserRoleIntegrationDataBuilder userRoleIntegrationDataBuilder;
//
//    @Value(value = "${api.version}")
//    private String apiVersion;
//
//    @Value(value = "${resource.users}")
//    private String usersResource;
//
//    @Value(value = "${resource.roles}")
//    private String rolesResource;
//
//    private UserIntegrationData userData;
//
//    private RoleIntegrationData roleData;
//
//    @BeforeEach
//    void setUp() {
//        StepVerifier.create(this.userRoleIntegrationDataBuilder.build())
//                .assertNext(userRoleData -> {
//                    this.userData = userRoleData.userIntegrationData();
//                    this.roleData = userRoleData.roleIntegrationData();
//                }).verifyComplete();
//    }
//
//    @Test
//    void testGetUsers() {
//        this.webTestClient.get().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.GET_ALL.formatted(WebTestMessages.USER))
//                .jsonPath("$.data").isArray()
//                .jsonPath("$.data[?(@.username=='jSalvador')].enabled").value(hasItem(true))
//                .jsonPath("$.data[?(@.username=='mruiz')].enabled").value(hasItem(true))
//                .jsonPath("$.data[?(@.username=='sfulgencio')].enabled").value(hasItem(false));
//    }
//
//    @Test
//    void testUsernameNotFound() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .queryParam("username", "afulgencio")
//                        .build())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND
//                        .formatted(WebTestMessages.USER, WebTestMessages.UserField.USERNAME, "afulgencio"));
//    }
//
//    @Test
//    void testUsernameFound() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                .queryParam("username", "jSalvador")
//                .build())
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.USER))
//                .jsonPath("$.data.enabled").isEqualTo(true)
//                .jsonPath("$.data.username").isEqualTo("jSalvador");
//    }
//
//    @Test
//    void testCreatedUserBadRequest() {
//        UserRequestV1 userRequestV1 = userRequestV1Factory(null, "afulgencio", "$#Antonio_F5401#$");
//
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRequestV1)
//                .exchange()
//                .expectStatus()
//                .isBadRequest()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.REQUIRED_FILE.formatted(
//                        WebTestMessages.USER, WebTestMessages.UserField.ENABLED));
//    }
//
//    @Test
//    void testAlreadyRegisteredUser() {
//        UserRequestV1 userRequestV1 = userRequestV1Factory(true, "jSalvador", "$$Jesus_AlphaOmega$$");
//
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRequestV1)
//                .exchange()
//                .expectStatus().isEqualTo(CONFLICT.value())
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(CONFLICT.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.ALREADY_REGISTERED.formatted(
//                        WebTestMessages.USER, WebTestMessages.UserField.USERNAME, userRequestV1.username()));
//    }
//
//    @Test
//    void testCreateUser() {
//        UserRequestV1 newUser = userRequestV1Factory(true, "afulgencio", "$#Antonio_F5401");
//
//        this.webTestClient.post().uri(getApiVersion())
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(newUser)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(CREATED.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.REGISTER_SUCCESSFULLY
//                        .formatted(WebTestMessages.USER))
//                .jsonPath("$.data.enabled").isEqualTo(newUser.enabled())
//                .jsonPath("$.data.username").isEqualTo(newUser.username());
//    }
//
//    @Test
//    void testUserIdInvalidInAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(this.roleData.dba());
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build("1a"))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(false)
//                .jsonPath("$.code").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.INVALID_ID
//                        .formatted(WebTestMessages.USER))
//                .jsonPath("$.data").isEmpty();
//    }
//
//    @Test
//    void testRoleIdIsRequiredInAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(null);
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build(this.userData.sfulgencio()))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Validation.Role.ID_NOT_NULL);
//    }
//
//    @Test
//    void testRoleNotFoundToAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(this.roleData.usr() + 10);
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build(this.userData.sfulgencio()))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND.formatted(
//                        WebTestMessages.ROLE, WebTestMessages.RoleField.ID, userRoleRequestV1.roleId()));
//    }
//
//    @Test
//    void testUserNotFoundToAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(this.roleData.admin());
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build(this.userData.sfulgencio() + 10))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND.formatted(
//                        WebTestMessages.USER, WebTestMessages.UserField.ID, this.userData.sfulgencio() + 10));
//    }
//
//    @Test
//    void testRelationshipExistsInAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(this.roleData.admin());
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build(this.userData.jSalvador()))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isEqualTo(CONFLICT.value())
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(CONFLICT.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.ALREADY_REGISTERED.formatted(
//                        WebTestMessages.UserRole.RELATIONSHIP, WebTestMessages.UserRole.IDS, this.userData.jSalvador() +
//                                WebTestMessages.COMMA + this.roleData.admin())
//                );
//    }
//
//    @Test
//    void testAssignRole() {
//        UserRoleRequestV1 userRoleRequestV1 = userRoleRequestV1Factory(this.roleData.dba());
//
//        this.webTestClient.post().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build(this.userData.sfulgencio()))
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue(userRoleRequestV1)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(CREATED.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.ASSIGNED_RELATIONSHIP.formatted(
//                        WebTestMessages.ROLE, this.roleData.dba(), WebTestMessages.USER, this.userData.sfulgencio()));
//    }
//
//    @Test
//    void testUserIdInvalidInFindRolesOfUser() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{id}")
//                        .path(this.rolesResource)
//                        .build("1a"))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(false)
//                .jsonPath("$.code").isEqualTo(BAD_REQUEST.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.INVALID_ID
//                        .formatted(WebTestMessages.USER))
//                .jsonPath("$.data").isEmpty();
//    }
//
//    @Test
//    void testUserNotFoundInFindRolesOfUser() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{userId}")
//                        .path(this.rolesResource)
//                        .build(this.userData.jSalvador() + 10))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.Error.NOT_FOUND
//                        .formatted(WebTestMessages.USER, WebTestMessages.UserField.ID, this.userData.jSalvador() + 10));
//    }
//
//    @Test
//    void testFindRolesOfUser() {
//        this.webTestClient.get().uri(uriBuilder -> uriBuilder.path(getApiVersion())
//                        .path("/{userId}")
//                        .path(this.rolesResource)
//                        .build(this.userData.jSalvador()))
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.flag").isEqualTo(true)
//                .jsonPath("$.code").isEqualTo(OK.value())
//                .jsonPath("$.message").isEqualTo(WebTestMessages.Message.FOUND.formatted(WebTestMessages.USER))
//                .jsonPath("$.data.id").isEqualTo(this.userData.jSalvador())
//                .jsonPath("$.data.username").isEqualTo("jSalvador")
//                .jsonPath("$.data.roles").isArray()
//                .jsonPath("$.data.roles[?(@.name=='Administrator')].abbreviation").isEqualTo("admin")
//                .jsonPath("$.data.roles[?(@.name=='Database Administrator')].abbreviation").isEqualTo("dba");
//    }
//
//    private String getApiVersion() {
//        return this.apiVersion + this.usersResource;
//    }
//}
