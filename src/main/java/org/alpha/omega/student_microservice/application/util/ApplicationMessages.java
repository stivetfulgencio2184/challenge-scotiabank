package org.alpha.omega.student_microservice.application.util;

public final class ApplicationMessages {

    public static final String STUDENT = "Student";
    public static final String USER = "User";
    public static final String ROLE = "Role";
    public static final String RELATIONSHIP = "Relationship";
    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String COMMA = ", ";

    private ApplicationMessages() {}

    public static class Exceptions {

        private Exceptions() {}

        public static final String ALREADY_REGISTERED = "%s with %s: %s already was registered.";
        public static final String NOT_FOUND = "%s with %s: %s was not found.";
    }
}
