package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.RoleIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.UserIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.UserRoleIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserRoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.userRoleEntityFactory;

@Component
public class UserRoleIntegrationDataBuilder {

    private final UserIntegrationDataBuilder userIntegrationDataBuilder;
    private final RoleIntegrationDataBuilder roleIntegrationDataBuilder;
    private final UserRoleRepository userRoleRepository;
    private final TransactionalOperator transactionalOperator;

    public UserRoleIntegrationDataBuilder(UserIntegrationDataBuilder userIntegrationDataBuilder,
                                          RoleIntegrationDataBuilder roleIntegrationDataBuilder,
                                          UserRoleRepository userRoleRepository,
                                          TransactionalOperator transactionalOperator) {
        this.userIntegrationDataBuilder = userIntegrationDataBuilder;
        this.roleIntegrationDataBuilder = roleIntegrationDataBuilder;
        this.userRoleRepository = userRoleRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<UserRoleIntegrationData> build() {
        return this.transactionalOperator
                .execute(status ->
                        cleanUserRoleTable()
                                .then(Mono.zip(
                                        this.userIntegrationDataBuilder.build(),
                                        this.roleIntegrationDataBuilder.build()
                                ))
                                .flatMap(tuple ->
                                        loadRelationships(tuple.getT1(), tuple.getT2()))
        ).single()
                .switchIfEmpty(Mono.error(new IllegalStateException("It could not be built the UserRoleIntegrationData class.")));
    }

    private Mono<Void> cleanUserRoleTable() {
        return this.userRoleRepository.deleteAll();
    }

    private Mono<UserRoleIntegrationData> loadRelationships(UserIntegrationData userData, RoleIntegrationData roleData) {
        return this.userRoleRepository.saveAll(List.of(
                        userRoleEntityFactory(userData.jSalvador(), roleData.admin()),
                        userRoleEntityFactory(userData.jSalvador(), roleData.dba()),
                        userRoleEntityFactory(userData.mruiz(), roleData.usr())))
                .then(Mono.just(new UserRoleIntegrationData(userData, roleData)));
    }
}
