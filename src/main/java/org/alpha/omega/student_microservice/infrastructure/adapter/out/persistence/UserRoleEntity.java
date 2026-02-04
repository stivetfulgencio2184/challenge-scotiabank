package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "user_roles")
public record UserRoleEntity(
        @Column(value = "user_id")
        Integer userId,
        @Column(value = "role_id")
        Integer roleId
) {
}
