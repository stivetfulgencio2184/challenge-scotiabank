package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant;

public final class WebMessages {

    private WebMessages() {}

    public static final String STUDENT = "Student";
    public static final String USER = "User";
    public static final String ROLE = "Role";
    public static final String PATH_DELIMITER = "/";

    public static class StudentField {

        private StudentField() {}

        public static final String STATUS = "status";
    }

    public static class UserField {

        private UserField() {}

        public static final String ID = "id";
        public static final String USER_ID = "id";
        public static final String ENABLED = "enabled";
        public static final String USERNAME = "username";
    }

    public static class RoleField {

        private RoleField() {}

        public static final String ID = "id";
        public static final String NAME = "name";
    }

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

        public static class Student {

            private Student() {}

            public static final String ID_NOT_NULL = "Student id field is required";
            public static final String NAME_NOT_BLANK = "Student name field is required";
            public static final String NAME_SIZE = "Student name should have between 2 and 50 characters";
            public static final String LASTNAME_NOT_BLANK = "Student lastname field is required";
            public static final String LASTNAME_SIZE = "Student lastname should have between 2 and 50 characters";
            public static final String STATUS_NOT_NULL = "Student status field is required";
            public static final String STATUS_PARAMETER = "Status parameter is invalid. Valid status: true or false";
            public static final String AGE_NOT_NULL = "Student age field is required";
        }

        public static class User {

            private User() {}

            public static final String ENABLED_NOT_NULL = "User enabled field is required";
            public static final String USERNAME_NOT_BLANK = "Username field must not be null neither empty";
            public static final String USERNAME_SIZE = "Username should have between 3 and 30 characters";
            public static final String PASSWORD_NOT_BLANK = "User password must not be null neither empty";
            public static final String PASSWORD_SIZE = "User password should have between 8 and 20 characters";
        }

        public static class Role {

            private Role() {}

            public static final String ID_NOT_NULL = "Role id field is required";
            public static final String NAME_PARAMETER = "Parameter name is required";
            public static final String NAME_NOT_BLANK = "Role name must not be null neither empty";
            public static final String NAME_SIZE = "Role name should have between 3 and 30 characters";
            public static final String ABBREVIATION_NOT_BLANK = "Role abbreviation must not be null neither empty";
            public static final String ABBREVIATION_SIZE = "Role abbreviation should have between 3 and 10 characters";
        }
    }

    public static class Message {

        private Message() {}

        public static final String GET_ALL = "All %s were recovered.";
        public static final String FOUND = "%s was found";
        public static final String REGISTER_SUCCESSFULLY = "%s was registered successfully";
        public static final String ASSIGNED_RELATIONSHIP = "%s with id: %s was assigned successfully at %s with id: %s";

        public static class Error {

            private Error() {}

            public static final String INVALID_ID = "Invalid %s id";
            public static final String DUPLICATE_STRATEGY = "Duplicate strategy for %s";
        }
    }
}
