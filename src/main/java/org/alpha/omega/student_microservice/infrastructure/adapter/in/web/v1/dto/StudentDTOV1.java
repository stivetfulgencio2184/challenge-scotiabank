package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;

public record StudentDTOV1(
        @NotNull(message = WebMessages.Validation.ID_NOT_NULL)
        Integer id,

        @NotBlank(message = WebMessages.Validation.NAME_NOT_BLANK)
        @Size(min = 2, max = 50, message = WebMessages.Validation.NAME_SIZE)
        String name,

        @NotBlank(message = WebMessages.Validation.LASTNAME_NOT_BLANK)
        @Size(min = 2, max = 50, message = WebMessages.Validation.LASTNAME_SIZE)
        String lastName,

        @NotNull(message = WebMessages.Validation.STATUS_NOT_NULL)
        Boolean status,

        @NotNull(message = WebMessages.Validation.AGE_NOT_NULL)
        Integer age
) {
}
