package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.router;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.RoleUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler.RoleHandlerV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoleRouterV1 {

    private final RoleUriFactory roleUriFactory;

    public RoleRouterV1(RoleUriFactory roleUriFactory) {
        this.roleUriFactory = roleUriFactory;
    }

    @Bean
    public RouterFunction<ServerResponse> roleRoutesV1(RoleHandlerV1 handler) {
        return route(GET(getBaseUri()),
                        handler::getRoles)
                .andRoute(GET(getBaseUri() + "/{id}"),
                        handler::findRoleById)
                .andRoute(POST(getBaseUri())
                        .and(contentType(APPLICATION_JSON)),
                        handler::createNewRole);
    }

    private String getBaseUri() {
        return this.roleUriFactory.getApiVersion() + this.roleUriFactory.getResourceRoles();
    }
}
