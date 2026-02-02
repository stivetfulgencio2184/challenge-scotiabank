package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.groupkey;

public record UserGroupKey(
        Integer id,
        String username,
        Boolean enabled
) {
}
