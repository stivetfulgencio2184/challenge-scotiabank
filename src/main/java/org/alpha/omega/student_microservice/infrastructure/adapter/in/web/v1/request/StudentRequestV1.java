package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;

public record StudentRequestV1(
        @NotNull(message = WebMessages.Validation.Student.ID_NOT_NULL)
        Integer id,

        @NotBlank(message = WebMessages.Validation.Student.NAME_NOT_BLANK)
        @Size(min = 2, max = 50, message = WebMessages.Validation.Student.NAME_SIZE)
        String name,

        @NotBlank(message = WebMessages.Validation.Student.LASTNAME_NOT_BLANK)
        @Size(min = 2, max = 50, message = WebMessages.Validation.Student.LASTNAME_SIZE)
        String lastName,

        @NotNull(message = WebMessages.Validation.Student.STATUS_NOT_NULL)
        Boolean status,

        @NotNull(message = WebMessages.Validation.Student.AGE_NOT_NULL)
        Integer age
) {
}
