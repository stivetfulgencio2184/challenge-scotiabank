package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant;

public final class WebMessages {

    private WebMessages() {}

    public static final String PATH_DELIMITER = "/";

    public static class Exceptions {

        private Exceptions() {}

        public static final String ALREADY_REGISTERED_EXCEPTION_BODY = """
            {"status":409,"error":"Already was registered","message":"%s"}
            """;
        public static final String BAD_REQUEST_EXCEPTION_BODY = """
            {"status":400,"error":"Bad Request","message":"%s"}
            """;
        public static final String INTERNAL_SERVER_ERROR_BODY = """
            {"status":500,"error":"Internal Server Error","message":"%s"}
            """;
        public static final String NOT_FOUND_EXCEPTION_BODY = """
            {"status":404,"error":"Not found","message":"%s"}
            """;
    }

    public static class Validation {

        private Validation() {}

        public static final String ID_NOT_NULL = "Student id field is required";
        public static final String NAME_NOT_BLANK = "Student name field is required";
        public static final String NAME_SIZE = "Student name should have between 2 and 50 characters";
        public static final String LASTNAME_NOT_BLANK = "Student lastname field is required";
        public static final String LASTNAME_SIZE = "Student lastname should have between 2 and 50 characters";
        public static final String STATUS_NOT_NULL = "Student status field is required";
        public static final String AGE_NOT_NULL = "Student age field is required";
    }
}
