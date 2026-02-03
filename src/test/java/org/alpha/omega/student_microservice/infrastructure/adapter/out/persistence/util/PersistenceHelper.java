package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.util;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.RoleEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistenceHelper {

    public static UserEntity userEntityFactory(Boolean enabled, String username, String password) {
        return new UserEntity(null, enabled, username, password);
    }

    public static RoleEntity roleEntityFactory(String name, String abbreviation) {
        return new RoleEntity(null, name, abbreviation);
    }

    public static StudentEntity studentEntityFactory(Integer id, String name, String lastname, Boolean status, Integer age) {
        return new StudentEntity(null, id, name, lastname, status, age);
    }

    public static User userFactory(String username, String password, Boolean enabled, Set<Role> roles) {
        return User.builder()
                .username(username)
                .password(password)
                .enabled(enabled)
                .roles(roles)
                .build();
    }

    public static Role roleFactory(String name, String abbreviation) {
        return Role.builder()
                .id(null)
                .name(name)
                .abbreviation(abbreviation)
                .build();
    }

    public static Student studentFactory(Integer id, String name, String lastname, Boolean status, Integer age) {
        return Student.builder()
                .id(id)
                .name(name)
                .lastName(lastname)
                .status(status)
                .age(age)
                .build();
    }

    public static void assertStudent(Student expected, Student actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getAge(), actual.getAge());
    }

    public static void assertStudentEntity(StudentEntity expected, Student actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expected.name(), actual.getName());
        assertEquals(expected.lastName(), actual.getLastName());
        assertEquals(expected.status(), actual.getStatus());
        assertEquals(expected.age(), actual.getAge());
    }

    public static void assertUser(String expectedUsername, String expectedPassword, User actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expectedUsername, actual.getUsername());
        assertEquals(expectedPassword, actual.getPassword());
        assertTrue(actual.getEnabled());
        assertNull(actual.getRoles());
    }

    public static void assertRole(String expectedName, String expectedAbbreviation, Role actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedAbbreviation, actual.getAbbreviation());
    }
}
