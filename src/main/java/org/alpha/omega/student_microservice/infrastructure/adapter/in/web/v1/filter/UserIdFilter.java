package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.filter;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebHelper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.FALSE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserIdFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request, @NonNull HandlerFunction<ServerResponse> next) {
        try {
            Integer.parseInt(request.pathVariable(WebMessages.UserField.USER_ID));
            return next.handle(request);
        } catch (NumberFormatException ex) {
            return ServerResponse.badRequest()
                    .contentType(APPLICATION_JSON)
                    .bodyValue(WebHelper.resultFactory(FALSE, BAD_REQUEST.value(),
                            WebMessages.Message.Error.INVALID_ID.formatted(WebMessages.USER), null));
        }
    }
}
