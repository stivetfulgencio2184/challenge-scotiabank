package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.strategy;

import org.alpha.omega.student_microservice.domain.exception.IllegalStateException;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExceptionStrategyResolver {

    private final Map<Class<? extends Throwable>, ExceptionStrategy<?>> strategies;

    public ExceptionStrategyResolver(List<ExceptionStrategy<?>> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ExceptionStrategy::exceptionType,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(String.format(WebMessages
                                    .Message.Error.DUPLICATE_STRATEGY, a.exceptionType()));
                        }
                ));
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> Mono<ExceptionStrategy<T>> resolve(T ex) {
        ExceptionStrategy<T> strategy = (ExceptionStrategy<T>) this.strategies.get(ex.getClass());
        return strategy == null ? Mono.empty() : Mono.just(strategy);
    }
}
