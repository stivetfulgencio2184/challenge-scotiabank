package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.exception;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy.ExceptionStrategy;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy.ExceptionStrategyResolver;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@Order(value = -2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ExceptionStrategyResolver resolver;

    public GlobalExceptionHandler(ExceptionStrategyResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        return this.resolver.resolve(ex)
                .switchIfEmpty(Mono.error(ex)) // if there is no strategy → propagate
                .flatMap(strategy -> invoke(strategy, exchange, ex))
                .onErrorResume(handlerError -> {
                    exchange.getResponse()
                            .setStatusCode(INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

    @SuppressWarnings(value = "unchecked")
    private <T extends Throwable> Mono<Void> invoke(ExceptionStrategy<T> strategy,
                                                    ServerWebExchange exchange, Throwable ex){
        return strategy.handle(exchange, (T) ex);
    }
}
