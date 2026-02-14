package org.alpha.omega.student_microservice.domain.model;

import java.util.Objects;
import java.util.Set;

/**
 * User class is immutable, because not define setter methods. This is very clean, by:
 * - Thread safety
 * - Predictability
 * - Better functional design
 * - Best for reactive programming
 */
public class User {

    private Integer id;
    private Boolean enabled;
    private String username;
    private String password;
    private Set<Role> roles;

    public Integer getId() {
        return id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Immutability Pattern: return a new instance with the password changed, without mutate the original object.
     * @param encryptPassword is the encoded password
     * @return User
     */
    public User encryptPassword(String encryptPassword) {
        return User.builder()
                .id(this.id)
                .enabled(this.enabled)
                .username(this.username)
                .password(encryptPassword)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            return false;
        } else return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", enabled=" + enabled +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    public static class UserBuilder {

        private Integer id;
        private Boolean enabled;
        private String username;
        private String password;
        private Set<Role> roles;

        private UserBuilder() {}

        public UserBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.enabled = this.enabled;
            user.username = this.username;
            user.password = this.password;
            user.roles = this.roles;
            return user;
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
