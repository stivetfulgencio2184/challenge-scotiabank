package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant;

public final class WebTestMessages {

    private WebTestMessages() {}

    public static class StudentDTOV1Field {

        private StudentDTOV1Field() {}

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LASTNAME = "lastName";
        public static final String STATUS = "status";
        public static final String AGE = "age";
    }

    public static class Exceptions {

        private Exceptions() {}

        public static final String ALREADY_REGISTERED = "Student with id: %s already was registered.";
        public static final String REQUIRED_FILE = "Student %s field is required";
    }
}
