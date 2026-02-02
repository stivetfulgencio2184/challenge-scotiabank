package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.handler;

import org.alpha.omega.student_microservice.application.port.in.StudentUseCase;
import org.alpha.omega.student_microservice.domain.exception.BadRequestException;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri.StudentUriFactory;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.StudentRequestV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper.StudentWebMapperV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.validation.GenericRequestValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class StudentHandlerV1 {

    private final StudentUseCase studentUseCase;
    private final StudentWebMapperV1 studentWebMapperV1;
    private final GenericRequestValidator validator;
    private final StudentUriFactory studentUriFactory;

    public StudentHandlerV1(StudentUseCase studentUseCase, StudentWebMapperV1 studentWebMapperV1, GenericRequestValidator validator, StudentUriFactory studentUriFactory) {
        this.studentUseCase = studentUseCase;
        this.studentWebMapperV1 = studentWebMapperV1;
        this.validator = validator;
        this.studentUriFactory = studentUriFactory;
    }

    public Mono<ServerResponse> getStudents(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam(WebMessages.StudentField.STATUS))
                .flatMap(this::findStudentsByStatus)
                .switchIfEmpty(getAllStudents());
    }

    private Mono<ServerResponse> getAllStudents() {
        return this.studentUseCase.getAllStudents()
                .map(this.studentWebMapperV1::toDtoV1)
                .collectList()
                .map(studentDTOV1s -> WebHelper.resultFactory(TRUE, OK.value(),
                        WebMessages.Message.GET_ALL.formatted(WebMessages.STUDENT), studentDTOV1s))
                .flatMap(result -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(result));
    }

    private Mono<ServerResponse> findStudentsByStatus(String status) {
        if(!status.equalsIgnoreCase("true") && !status.equalsIgnoreCase("false"))
            return Mono.error(new BadRequestException(WebMessages.Validation.Student.STATUS_PARAMETER));

        return this.studentUseCase.getStudentsByStatus(Boolean.parseBoolean(status))
                .map(this.studentWebMapperV1::toDtoV1)
                .collectList()
                .map(studentDTOV1s -> WebHelper.resultFactory(TRUE, OK.value(),
                        WebMessages.Message.FOUND.formatted(WebMessages.STUDENT), studentDTOV1s))
                .flatMap(result -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(result));
    }

    public Mono<ServerResponse> createNewStudent(ServerRequest request) {
        return request.bodyToMono(StudentRequestV1.class)
                .doOnNext(this.validator::validate)
                .map(this.studentWebMapperV1::toDomain)
                .flatMap(this.studentUseCase::createStudent)
                .map(this.studentWebMapperV1::toDtoV1)
                .flatMap(this::buildCreatedResponse);
    }

    private Mono<ServerResponse> buildCreatedResponse(StudentRequestV1 studentRequestV1) {
        return ServerResponse.created(this.studentUriFactory.uriLocation(String.valueOf(studentRequestV1.id())))
                .contentType(APPLICATION_JSON)
                .bodyValue(WebHelper.resultFactory(TRUE, CREATED.value(),
                        WebMessages.Message.REGISTER_SUCCESSFULLY.formatted(WebMessages.STUDENT),
                        studentRequestV1));
    }
}
