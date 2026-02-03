package org.alpha.omega.student_microservice.application.util;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.domain.model.User;
import org.mockito.Mockito;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ServiceHelper {

    public static Role roleFactory(Integer id, String name, String abbreviation) {
        return Role.builder()
                .id(id)
                .name(name)
                .abbreviation(abbreviation)
                .build();
    }

    public static Student studentFactory(Integer studentId, Boolean status, Integer age) {
        return Student.builder()
                .id(studentId)
                .name("Name " + studentId)
                .lastName("Last Name " + studentId)
                .status(status)
                .age(age)
                .build();
    }

    public static User userFactory(Integer id, String username, Boolean enabled, Set<Role> roles) {
        return User.builder()
                .id(id)
                .username(username)
                .enabled(enabled)
                .password("password" + username)
                .roles(roles)
                .build();
    }

    public static void assertRole(Role expected, Role actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAbbreviation(), actual.getAbbreviation());
    }

    public static void assertUser(User expected, User actual) {
        assertThat(actual.getId()).isPositive();
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEnabled(), actual.getEnabled());
        assertNull(actual.getRoles());
    }

    public static void enableTransactionalExecution(TransactionalOperator transactionalOperator) {
        ReactiveTransaction transaction = mock(ReactiveTransaction.class);

        given(transactionalOperator.execute(Mockito.<TransactionCallback<?>>any()))
                .willAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return Flux.from(callback.doInTransaction(transaction));
                });
    }
}
