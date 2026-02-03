package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;

public record UserRequestV1(

        @NotNull(message = WebMessages.Validation.User.ENABLED_NOT_NULL)
        Boolean enabled,

        @NotBlank(message = WebMessages.Validation.User.USERNAME_NOT_BLANK)
        @Size(min = 3, max = 30, message = WebMessages.Validation.User.USERNAME_SIZE)
        String username,

        @NotBlank(message = WebMessages.Validation.User.PASSWORD_NOT_BLANK)
        @Size(min = 8, max = 20, message = WebMessages.Validation.User.PASSWORD_SIZE)
        String password
) {
}
