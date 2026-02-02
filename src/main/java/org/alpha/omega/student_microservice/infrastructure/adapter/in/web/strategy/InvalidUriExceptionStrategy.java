package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.alpha.omega.student_microservice.domain.exception.InvalidUriException;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class InvalidUriExceptionStrategy implements ExceptionStrategy<InvalidUriException>{

    @Override
    public Class<InvalidUriException> exceptionType() {
        return InvalidUriException.class;
    }

    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull InvalidUriException ex) {
        var response = exchange.getResponse();
        if (response.isCommitted())
            return Mono.error(ex);
        response.setStatusCode(INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(APPLICATION_JSON);

        var body = WebMessages.Exceptions.INTERNAL_SERVER_ERROR_BODY.formatted(ex.getMessage());

        return response.writeWith(Mono.fromSupplier(() -> response.bufferFactory().wrap(body.getBytes(UTF_8))));
    }
}
