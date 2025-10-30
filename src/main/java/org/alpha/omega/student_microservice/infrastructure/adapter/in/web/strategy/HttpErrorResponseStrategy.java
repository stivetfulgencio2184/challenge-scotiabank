package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface HttpErrorResponseStrategy {

    Boolean support(Throwable ex);
    Mono<Void> handle(ServerWebExchange exchange, Throwable ex);
}
