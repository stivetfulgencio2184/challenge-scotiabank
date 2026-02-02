package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.alpha.omega.student_microservice.domain.exception.NotFoundException;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class NotFoundExceptionStrategy implements ExceptionStrategy<NotFoundException> {

    @Override
    public Class<NotFoundException> exceptionType() {
        return NotFoundException.class;
    }

    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull NotFoundException ex) {
        var response = exchange.getResponse();
        if (response.isCommitted())
            return Mono.error(ex);
        response.setStatusCode(NOT_FOUND);
        response.getHeaders().setContentType(APPLICATION_JSON);

        var body = WebMessages.Exceptions.NOT_FOUND_EXCEPTION_BODY.formatted(ex.getMessage());

        return response.writeWith(Mono.fromSupplier(() -> response.bufferFactory().wrap(body.getBytes(UTF_8))));
    }
}
