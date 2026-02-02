package org.alpha.omega.student_microservice.domain.exception;

public class InvalidUriException extends RuntimeException {

    public InvalidUriException(String message) {
        super(message);
    }
}
