package org.alpha.omega.student_microservice.infrastructure.adapter.in.security.contant;

public final class SecurityConstant {

    private SecurityConstant() {}

    public static class Argon2 {

        private Argon2() {}

        public static final int SALT_LENGTH = 16;
        public static final int HASH_LENGTH = 32;
        public static final int PARALLELISM = 1;
        public static final int MEMORY = 65536;
        public static final int ITERATIONS = 3;
    }

    public static class BCrypt {

        private BCrypt() {}

        public static final int STRENGTH = 12;
    }
}
