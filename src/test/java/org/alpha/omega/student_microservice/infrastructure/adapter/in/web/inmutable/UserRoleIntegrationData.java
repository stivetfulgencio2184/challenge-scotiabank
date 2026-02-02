package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable;

public record UserRoleIntegrationData(
        UserIntegrationData userIntegrationData,
        RoleIntegrationData roleIntegrationData
) {
}
