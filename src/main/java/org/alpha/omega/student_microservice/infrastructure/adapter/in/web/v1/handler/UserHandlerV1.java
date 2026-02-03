package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler;

//import org.alpha.omega.student_microservice.application.port.in.UserRoleUseCase;
import org.alpha.omega.student_microservice.application.port.in.UserUseCase;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.RoleUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.UserUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper.UserWebMapperV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRequestV1;
//import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRoleRequestV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response.UserResponseV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.validation.GenericRequestValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.TRUE;
import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper.foundResponse;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserHandlerV1 {

//    private final UserUseCase userUseCase;
//    private final UserRoleUseCase userRoleUseCase;
//    private final UserWebMapperV1 userWebMapperV1;
//    private final GenericRequestValidator validator;
//    private final UserUriFactory userUriFactory;
//    private final RoleUriFactory roleUriFactory;
//
//    public UserHandlerV1(UserUseCase userUseCase, UserRoleUseCase userRoleUseCase, UserWebMapperV1 userWebMapperV1,
//                         GenericRequestValidator validator, UserUriFactory userUriFactory, RoleUriFactory roleUriFactory) {
//        this.userUseCase = userUseCase;
//        this.userRoleUseCase = userRoleUseCase;
//        this.userWebMapperV1 = userWebMapperV1;
//        this.validator = validator;
//        this.userUriFactory = userUriFactory;
//        this.roleUriFactory = roleUriFactory;
//    }
//
//    public Mono<ServerResponse> getUsers(ServerRequest request) {
//        return Mono.justOrEmpty(request.queryParam(WebMessages.UserField.USERNAME))
//                .flatMap(this::findUserByUsername)
//                .switchIfEmpty(getAllUsers());
//    }
//
//    private Mono<ServerResponse> getAllUsers() {
//        return this.userUseCase.getAllUsers()
//                .map(this.userWebMapperV1::toResponseV1)
//                .collectList()
//                .map(users -> WebHelper.resultFactory(
//                        TRUE, OK.value(), WebMessages.Message.GET_ALL.formatted(WebMessages.USER), users))
//                .flatMap(result -> ServerResponse.ok()
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(result));
//    }
//
//    private Mono<ServerResponse> findUserByUsername(String username) {
//        return foundResponse(this.userUseCase.getUserByUsername(username), this.userWebMapperV1::toResponseV1,
//                WebMessages.Message.FOUND.formatted(WebMessages.USER));
//    }
//
//    public Mono<ServerResponse> createNewUser(ServerRequest request) {
//        return request.bodyToMono(UserRequestV1.class)
//                .doOnNext(validator::validate)
//                .map(this.userWebMapperV1::toDomain)
//                .flatMap(this.userUseCase::createUser)
//                .map(this.userWebMapperV1::toResponseV1)
//                .flatMap(this::buildCreatedResponse);
//    }
//
//    private Mono<ServerResponse> buildCreatedResponse(UserResponseV1 userResponseV1) {
//        return ServerResponse.created(
//                        this.userUriFactory.uriLocation(String.valueOf(userResponseV1.id())))
//                .contentType(APPLICATION_JSON)
//                .bodyValue(WebHelper.resultFactory(TRUE, CREATED.value(),
//                        WebMessages.Message.REGISTER_SUCCESSFULLY.formatted(WebMessages.USER),
//                        userResponseV1));
//    }
//
//    public Mono<ServerResponse> findRolesOfUser(ServerRequest request) {
//        Integer userId = Integer.parseInt(request.pathVariable(WebMessages.UserField.USER_ID));
//
//        return foundResponse(this.userRoleUseCase.getRolesByUser(userId), this.userWebMapperV1::toResponseV1,
//                WebMessages.Message.FOUND.formatted(WebMessages.USER));
//    }
//
//    public Mono<ServerResponse> assignRole(ServerRequest request) {
//        Integer userId = Integer.parseInt(request.pathVariable(WebMessages.UserField.USER_ID));
//
//        return request.bodyToMono(UserRoleRequestV1.class)
//                .doOnNext(validator::validate)
//                .flatMap(userRoleRequestV1 -> this.userRoleUseCase.assignRoleToUser(userId,
//                                userRoleRequestV1.roleId())
//                        .thenReturn(userRoleRequestV1))
//                .flatMap(userRoleRequestV1 -> buildAssignResponse(userRoleRequestV1,
//                        userId + this.roleUriFactory.getResourceRoles(), userId));
//    }
//
//    private Mono<ServerResponse> buildAssignResponse(UserRoleRequestV1 userRoleRequestV1, String path, Integer userId) {
//        return ServerResponse.created(
//                        this.userUriFactory.uriLocation(path))
//                .contentType(APPLICATION_JSON)
//                .bodyValue(WebHelper.resultFactory(TRUE, CREATED.value(),
//                        WebMessages.Message.ASSIGNED_RELATIONSHIP.formatted(WebMessages.ROLE,
//                                userRoleRequestV1.roleId(), WebMessages.USER, userId), null));
//    }
}
