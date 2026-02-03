package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;

public record RoleRequestV1(

        @NotBlank(message = WebMessages.Validation.Role.NAME_NOT_BLANK)
        @Size(min = 3, max = 30, message = WebMessages.Validation.Role.NAME_SIZE)
        String name,

        @NotBlank(message = WebMessages.Validation.Role.ABBREVIATION_NOT_BLANK)
        @Size(min = 3, max = 10, message = WebMessages.Validation.Role.ABBREVIATION_SIZE)
        String abbreviation
) {
}
