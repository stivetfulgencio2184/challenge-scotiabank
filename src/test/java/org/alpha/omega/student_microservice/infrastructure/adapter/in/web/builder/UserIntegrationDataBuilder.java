package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable.UserIntegrationData;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.userEntityFactory;

@Component
public class UserIntegrationDataBuilder {

    private final UserRepository userRepository;

    public UserIntegrationDataBuilder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserIntegrationData> build() {
        return cleanUserTable()
                .then(loadUsers())
                .map(this::toContext);
    }

    private Mono<Void> cleanUserTable() {
        return this.userRepository.deleteAll();
    }

    private Mono<Map<String, Integer>> loadUsers() {
        return this.userRepository.saveAll(List.of(
                        userEntityFactory(true, "jSalvador", "$$Jesus_AlphaOmega$$"),
                        userEntityFactory(true, "mruiz", "$Mary_0710$"),
                        userEntityFactory(false, "sfulgencio", "$Stivet_2184$")))
                .collectMap(UserEntity::username, UserEntity::id);
    }

    private UserIntegrationData toContext(Map<String, Integer> users) {
        return new UserIntegrationData(
                users.get("jSalvador"),
                users.get("mruiz"),
                users.get("sfulgencio")
        );
    }
}
