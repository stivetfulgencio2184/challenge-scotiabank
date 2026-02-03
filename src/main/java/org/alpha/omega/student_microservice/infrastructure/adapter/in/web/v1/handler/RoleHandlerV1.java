package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler;

import org.alpha.omega.student_microservice.application.port.in.RoleUseCase;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.RoleUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper.RoleWebMapperV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.RoleRequestV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response.RoleResponseV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.validation.GenericRequestValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper.foundResponse;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class RoleHandlerV1 {

    private final RoleUseCase roleUseCase;
    private final RoleWebMapperV1 roleWebMapperV1;
    private final GenericRequestValidator validator;
    private final RoleUriFactory roleUriFactory;

    public RoleHandlerV1(RoleUseCase roleUseCase, RoleWebMapperV1 roleWebMapperV1, GenericRequestValidator validator,
                         RoleUriFactory roleUriFactory) {
        this.roleUseCase = roleUseCase;
        this.roleWebMapperV1 = roleWebMapperV1;
        this.validator = validator;
        this.roleUriFactory = roleUriFactory;
    }

    public Mono<ServerResponse> getRoles(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam(WebMessages.RoleField.NAME))
                .flatMap(this::findRoleByName)
                .switchIfEmpty(getAllRoles());
    }

    private Mono<ServerResponse> getAllRoles() {
        return this.roleUseCase.getAllRoles()
                .map(this.roleWebMapperV1::toResponseV1)
                .collectList()
                .map(roles -> WebHelper.resultFactory(
                        TRUE, OK.value(), WebMessages.Message.GET_ALL.formatted(WebMessages.ROLE), roles))
                .flatMap(result -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(result));
    }

    private Mono<ServerResponse> findRoleByName(String name) {
        return foundResponse(this.roleUseCase.getRoleByName(name), this.roleWebMapperV1::toResponseV1,
                WebMessages.Message.FOUND.formatted(WebMessages.ROLE));
    }

    public Mono<ServerResponse> findRoleById(ServerRequest request) {
        String roleIdStr = request.pathVariable(WebMessages.RoleField.ID);
        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest()
                    .bodyValue(WebHelper.resultFactory(FALSE, BAD_REQUEST.value(),
                            WebMessages.Message.Error.INVALID_ID.formatted(WebMessages.ROLE), null));
        }
        return foundResponse(this.roleUseCase.getRoleById(roleId), this.roleWebMapperV1::toResponseV1,
                WebMessages.Message.FOUND.formatted(WebMessages.ROLE));
    }

    public Mono<ServerResponse> createNewRole(ServerRequest request) {
        return request.bodyToMono(RoleRequestV1.class)
                .doOnNext(this.validator::validate)
                .map(this.roleWebMapperV1::toDomain)
                .flatMap(this.roleUseCase::createRole)
                .map(this.roleWebMapperV1::toResponseV1)
                .flatMap(this::buildCreatedResponse);
    }

    private Mono<ServerResponse> buildCreatedResponse(RoleResponseV1 roleResponseV1) {
        return ServerResponse.created(this.roleUriFactory.uriLocation(String.valueOf(roleResponseV1.id())))
                .contentType(APPLICATION_JSON)
                .bodyValue(WebHelper.resultFactory(TRUE, CREATED.value(),
                        WebMessages.Message.REGISTER_SUCCESSFULLY.formatted(WebMessages.ROLE),
                        roleResponseV1));
    }
}
