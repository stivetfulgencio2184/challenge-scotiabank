package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.alpha.omega.student_microservice.domain.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GenericRequestValidator {

    private final Validator validator;

    public GenericRequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object) {
        var violations = this.validator.validate(object);

        if (!violations.isEmpty()) {
            throw new BadRequestException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }
    }
}
