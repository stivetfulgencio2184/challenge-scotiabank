package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.inmutable;

public record RoleIntegrationData(
        Integer admin,
        Integer dba,
        Integer usr
) {
}
