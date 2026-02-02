package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant;

public final class WebTestMessages {

    private WebTestMessages() {}

    public static final String STUDENT = "Student";
    public static final String USER = "User";
    public static final String ROLE = "Role";
    public static final String COMMA = ", ";

    public static class StudentDTOV1Field {

        private StudentDTOV1Field() {}

        public static final String ID = "id";
    }

    public static class UserField {

        private UserField() {}

        public static final String ID = "id";
        public static final String ENABLED = "enabled";
        public static final String USERNAME = "username";
    }

    public static class RoleField {

        private RoleField() {}

        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static class UserRole {

        private UserRole() {}

        public static final String IDS = "ids";
        public static final String RELATIONSHIP = "Relationship";
    }

    public static class Validation {

        private Validation() {}

        public static class Student {

            private Student() {}

            public static final String STATUS_PARAMETER = "Status parameter is invalid. Valid status: true or false";
        }

        public static class Role {

            private Role() {}

            public static final String ID_NOT_NULL = "Role id field is required";
            public static final String NAME_NOT_BLANK = "Role name must not be null neither empty";
            public static final String ABBREVIATION_NOT_BLANK = "Role abbreviation must not be null neither empty";
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

            public static final String ALREADY_REGISTERED = "%s with %s: %s already was registered.";
            public static final String REQUIRED_FILE = "%s %s field is required";
            public static final String NOT_FOUND = "%s with %s: %s was not found.";
            public static final String INVALID_ID = "Invalid %s id";
        }
    }
}
