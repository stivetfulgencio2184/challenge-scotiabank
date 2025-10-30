package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class DefaultExceptionStrategy implements HttpErrorResponseStrategy{

    @Override
    public Boolean support(Throwable ex) {
        return true;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();
        response.setStatusCode(INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(APPLICATION_JSON);

        var body = String.format(WebMessages.Exceptions.INTERNAL_SERVER_ERROR_BODY, ex.getMessage());

        var buffer = response.bufferFactory().wrap(body.getBytes(UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
