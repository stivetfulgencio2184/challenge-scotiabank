package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.alpha.omega.student_microservice.application.port.in.StudentUseCase;
import org.alpha.omega.student_microservice.domain.exception.BadRequestException;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.dto.StudentDTOV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper.StudentWebMapperV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class StudentHandlerV1 {

    private final StudentUseCase studentUseCase;
    private final StudentWebMapperV1 studentWebMapperV1;
    private final Validator validator;

    @Value(value = "${api.version}")
    private String apiVersion;

    @Value(value = "${resource}")
    private String resource;

    public StudentHandlerV1(StudentUseCase studentUseCase, StudentWebMapperV1 studentWebMapperV1, Validator validator) {
        this.studentUseCase = studentUseCase;
        this.studentWebMapperV1 = studentWebMapperV1;
        this.validator = validator;
    }

    public Mono<ServerResponse> getStudents() {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(this.studentUseCase.getAllStudents()
                        .map(this.studentWebMapperV1::toDtoV1), Student.class);
    }

    public Mono<ServerResponse> findStudentsByStatus(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(this.studentUseCase
                        .getStudentsByStatus(Boolean.parseBoolean(request.pathVariable("status")))
                                .map(this.studentWebMapperV1::toDtoV1), Student.class);
    }

    public Mono<ServerResponse> createNewStudent(ServerRequest request) {
        return request.bodyToMono(StudentDTOV1.class)
                .flatMap(studentRequest -> {
                    var violations = validator.validate(studentRequest);

                    if(!violations.isEmpty()) {
                        String errorMessages = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        return Mono.error(new BadRequestException(errorMessages));
                    }
                    return this.studentUseCase.createStudent(StudentWebMapperV1.INSTANCE.toDomain(studentRequest))
                            .map(this.studentWebMapperV1::toDtoV1)
                                .flatMap(registeredStudent -> {
                                       try {
                                        var uri = new URI(this.apiVersion + this.resource +
                                                WebMessages.PATH_DELIMITER + registeredStudent.id());
                                        return ServerResponse.created(uri)
                                                .body(BodyInserters.fromValue(registeredStudent));
                                       }catch (URISyntaxException e){
                                           return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                                       }
                                });
                });
    }
}
