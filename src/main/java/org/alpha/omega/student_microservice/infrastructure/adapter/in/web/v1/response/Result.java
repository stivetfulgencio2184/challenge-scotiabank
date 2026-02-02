package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response;

import lombok.Builder;

@Builder
public record Result(
        Boolean flag, // Two values: true means success, false means not success
        Integer code, // Status code, e.g. 200
        String message, // Response message
        Object data // The response payload
) {
}
