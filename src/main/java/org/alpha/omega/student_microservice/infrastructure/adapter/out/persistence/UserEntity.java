package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "users")
public record UserEntity(
        @Id
        Integer id,
        Boolean enabled,
        String username,
        String password
) {
}
