package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.router;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.RoleUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.UserUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.filter.UserIdFilter;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler.UserHandlerV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterV1 {

    private final UserUriFactory userUriFactory;
    private final RoleUriFactory roleUriFactory;

    public UserRouterV1(UserUriFactory userUriFactory, RoleUriFactory roleUriFactory) {
        this.userUriFactory = userUriFactory;
        this.roleUriFactory = roleUriFactory;
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutesV1(UserHandlerV1 handler, UserIdFilter userIdFilter) {
        RouterFunction<ServerResponse> routesWithoutFilter = route(GET(getBaseUri()), handler::getUsers)
                .andRoute(POST(getBaseUri())
                                .and(contentType(APPLICATION_JSON)), handler::createNewUser);
        RouterFunction<ServerResponse> routesWithFilter = route(GET(getUsersRolesPath()), handler::findRolesOfUser)
                .andRoute(POST(getUsersRolesPath())
                                .and(contentType(APPLICATION_JSON)), handler::assignRole)
                .filter(userIdFilter);

        return routesWithoutFilter
                .and(routesWithFilter);
    }

    private String getBaseUri() {
        return this.userUriFactory.getApiVersion() + this.userUriFactory.getResourceUsers();
    }

    private String getUsersRolesPath() { return getBaseUri() + "/{id}" + this.roleUriFactory.getResourceRoles();}
}
