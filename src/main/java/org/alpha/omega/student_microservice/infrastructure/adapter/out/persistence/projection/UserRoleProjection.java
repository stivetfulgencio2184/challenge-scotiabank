package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.projection;

public record UserRoleProjection(
        Integer userId,
        Boolean enabled,
        String userName,
        Integer roleId,
        String roleName,
        String abbreviation
) {
}
