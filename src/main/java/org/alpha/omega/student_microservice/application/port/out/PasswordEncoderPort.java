package org.alpha.omega.student_microservice.application.port.out;

public interface PasswordEncoderPort {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodePassword);
}
