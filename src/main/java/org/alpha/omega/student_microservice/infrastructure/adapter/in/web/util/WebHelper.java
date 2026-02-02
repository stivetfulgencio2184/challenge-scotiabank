package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response.Result;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class WebHelper {

    private WebHelper() {}

    public static Result resultFactory(Boolean flag, Integer code, String message, Object data) {
        return Result.builder()
                .flag(flag)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T, R> Mono<ServerResponse> foundResponse(Mono<T> source, Function<T, R> mapper, String message) {
        return source
                .map(mapper)
                .map(data -> WebHelper.resultFactory(TRUE, OK.value(), message, data))
                .flatMap(result -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(result));
    }
}
