package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request;

import jakarta.validation.constraints.NotNull;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;

public record UserRoleRequestV1(

        @NotNull(message = WebMessages.Validation.Role.ID_NOT_NULL)
        Integer roleId
) {
}
