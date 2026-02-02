package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ExceptionStrategy<T extends Throwable> {

    Class<T> exceptionType();
    Mono<Void> handle(ServerWebExchange exchange, T ex);
}
