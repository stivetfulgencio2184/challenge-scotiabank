package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "roles")
public record RoleEntity(
        @Id
        Integer id,
        String name,
        String abbreviation
) {
}
