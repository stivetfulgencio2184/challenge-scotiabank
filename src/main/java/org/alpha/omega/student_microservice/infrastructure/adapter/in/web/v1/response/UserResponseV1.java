package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response;

import java.util.Set;

public record UserResponseV1(
        Integer id,
        String username,
        Boolean enabled,
        Set<RoleResponseV1> roles
) {
}
