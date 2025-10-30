package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.router;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler.StudentHandlerV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StudentRouterV1 {

    @Value(value = "${api.version}")
    private String apiVersion;

    @Value(value = "${resource}")
    private String resource;

    @Bean
    public RouterFunction<ServerResponse> studentRoutesV1(StudentHandlerV1 handler) {
        return route(GET(this.apiVersion + this.resource)
                        .and(accept(APPLICATION_JSON)),
                    request -> handler.getStudents())
                .andRoute(GET(this.apiVersion + this.resource + "/{status}")
                                .and(accept(APPLICATION_JSON)),
                        handler::findStudentsByStatus)
                .andRoute(POST(this.apiVersion + this.resource)
                                .and(accept(APPLICATION_JSON))
                                .and(contentType(APPLICATION_JSON)),
                        handler::createNewStudent);
    }
}
