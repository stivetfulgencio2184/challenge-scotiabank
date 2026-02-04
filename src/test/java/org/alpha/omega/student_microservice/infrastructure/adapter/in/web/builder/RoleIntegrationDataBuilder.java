package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.RoleIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.RoleEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.RoleRepository;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserRoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.roleEntityFactory;

@Component
public class RoleIntegrationDataBuilder {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final TransactionalOperator transactionalOperator;

    public RoleIntegrationDataBuilder(UserRoleRepository userRoleRepository, RoleRepository roleRepository,
                                      TransactionalOperator transactionalOperator) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<RoleIntegrationData> build() {
        return this.transactionalOperator
                .execute(status -> cleanUserRoleTable()
                        .then(cleanRoleTable())
                        .then(loadRoles())
                        .map(this::toContext)
                ).single()
                .switchIfEmpty(Mono.error(new IllegalStateException("It could not be built the RoleIntegrationData class.")));
    }

    private Mono<Void> cleanUserRoleTable() {
        return this.userRoleRepository.deleteAll();
    }

    private Mono<Void> cleanRoleTable() {
        return this.roleRepository.deleteAll();
    }

    private Mono<Map<String, Integer>> loadRoles() {
        return this.roleRepository.saveAll(List.of(
                roleEntityFactory("Administrator", "admin"),
                roleEntityFactory("Database Administrator", "dba"),
                roleEntityFactory("User", "usr")
        )).collectMap(RoleEntity::abbreviation, RoleEntity::id);
    }

    private RoleIntegrationData toContext(Map<String, Integer> roles) {
        return new RoleIntegrationData(
                roles.get("admin"),
                roles.get("dba"),
                roles.get("usr")
        );
    }
}
