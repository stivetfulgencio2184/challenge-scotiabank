package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.exception;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy.HttpErrorResponseStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(value = -2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final List<HttpErrorResponseStrategy> strategies;

    public GlobalExceptionHandler(List<HttpErrorResponseStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return this.strategies.stream()
                .filter(strategy -> strategy.support(ex))
                .findFirst()
                .orElseGet(() -> strategies.stream()
                        .filter(s -> s.support(new Exception()))
                        .findFirst()
                        .orElseThrow())
                .handle(exchange, ex);
    }
}
