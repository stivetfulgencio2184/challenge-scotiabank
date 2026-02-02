package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.router;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.StudentUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler.StudentHandlerV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StudentRouterV1 {

    private final StudentUriFactory studentUriFactory;

    public StudentRouterV1(StudentUriFactory studentUriFactory) {
        this.studentUriFactory = studentUriFactory;
    }

    @Bean
    public RouterFunction<ServerResponse> studentRoutesV1(StudentHandlerV1 handler) {
        return route(GET(getBaseUri()), handler::getStudents)
                .andRoute(POST(getBaseUri())
                                .and(contentType(APPLICATION_JSON)),
                        handler::createNewStudent);
    }

    private String getBaseUri() {
        return this.studentUriFactory.getApiVersion() + this.studentUriFactory.getResourceStudents();
    }
}
