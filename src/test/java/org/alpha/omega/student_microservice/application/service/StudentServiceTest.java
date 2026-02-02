package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.out.StudentRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationMessages;
import org.alpha.omega.student_microservice.application.util.ApplicationTestMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.alpha.omega.student_microservice.application.util.ServiceHelper.enableTransactionalExecution;
import static org.alpha.omega.student_microservice.application.util.ServiceHelper.studentFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepositoryPort repositoryPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testStudentRegistered() {
        //Given
        Student studentRegistered = studentFactory(1, true, 12);
        Integer studentId = studentRegistered.getId();
        given(this.repositoryPort.findByStudentId(studentId))
                .willReturn(Mono.just(studentRegistered));

        //When and Then
        StepVerifier.create(this.studentService.isStudentRegistered(studentId))
                .expectNext(true)
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStudentId(studentId);
    }

    @Test
    void testStudentNotRegistered() {
        //Given
        given(this.repositoryPort.findByStudentId(777))
                .willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.studentService.isStudentRegistered(777))
                .expectNext(false)
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStudentId(777);
    }

    @Test
    void testStudentAlreadyRegistered() {
        //Given
        Student studentRegistered = studentFactory(1, true, 23);
        Integer studentId = studentRegistered.getId();
        given(this.repositoryPort.findByStudentId(studentId))
                .willReturn(Mono.just(studentRegistered));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.studentService.createStudent(studentRegistered))
                .expectErrorSatisfies(error ->
                    assertThat(error)
                            .isInstanceOf(AlreadyRegisteredException.class)
                            .hasMessage(String.format(
                                    ApplicationTestMessages.Exceptions.ALREADY_REGISTERED, ApplicationMessages.STUDENT,
                                    ApplicationTestMessages.ID, studentRegistered.getId()))
                ).verify();
        then(this.repositoryPort).should(times(1)).findByStudentId(studentId);
        then(this.repositoryPort).should(never()).save(any());
    }

    @Test
    void testCreateStudent() {
        //Given
        Student studentToRegister = studentFactory(1, true, 24);
        Integer studentId = studentToRegister.getId();
        given(this.repositoryPort.findByStudentId(studentId))
                .willReturn(Mono.empty());
        given(this.repositoryPort.save(studentToRegister)).willReturn(Mono.just(studentToRegister));
        enableTransactionalExecution(this.transactionalOperator);

        //When and Then
        StepVerifier.create(this.studentService.createStudent(studentToRegister))
                .assertNext(student -> assertEquals(studentToRegister, student))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStudentId(studentId);
        then(this.repositoryPort).should(times(1)).save(studentToRegister);
    }

    @Test
    void testGetAllStudents() {
        //Given
        Student sfulgencio = studentFactory(1, true, 41);
        Student mruiz = studentFactory(2, false, 30);
        List<Student> expectedStudents = List.of(sfulgencio, mruiz);
        given(this.repositoryPort.findAll()).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.studentService.getAllStudents()
                        .collectList())
                .assertNext(students -> {
                    assertEquals(expectedStudents.size(), students.size());
                    Set<String> names = students.stream()
                            .map(Student::getName)
                            .collect(Collectors.toSet());
                    assertTrue(names.contains(sfulgencio.getName()));
                    assertTrue(names.contains(mruiz.getName()));
                })
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findAll();
    }

    @Test
    void testGetStudentWithActiveStatus() {
        //Given
        Student sfulgencio = studentFactory(1, true, 35);
        Student mruiz = studentFactory(2, true, 24);
        List<Student> expectedStudents = List.of(sfulgencio, mruiz);
        given(this.repositoryPort.findByStatus(true)).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.studentService.getStudentsByStatus(true)
                        .collectList())
                .assertNext(students -> {
                    assertEquals(expectedStudents.size(), students.size());
                    Set<String> names = students.stream()
                            .map(Student::getName)
                            .collect(Collectors.toSet());
                    assertTrue(names.contains(sfulgencio.getName()));
                    assertTrue(names.contains(mruiz.getName()));
                })
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStatus(true);
    }

    @Test
    void testGetStudentWithActiveStatusEmpty() {
        //Given
        given(this.repositoryPort.findByStatus(true)).willReturn(Flux.empty());

        //When and Then
        StepVerifier.create(this.studentService.getStudentsByStatus(true))
                .expectNextCount(0)
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStatus(true);
    }

    @Test
    void testGetStudentWithInactiveStatus() {
        //Given
        Student sfulgencio = studentFactory(1, false, 27);
        Student mruiz = studentFactory(2, false, 16);
        List<Student> expectedStudents = List.of(sfulgencio, mruiz);
        given(this.repositoryPort.findByStatus(false)).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.studentService.getStudentsByStatus(false)
                        .collectList())
                .assertNext(students -> {
                    assertEquals(expectedStudents.size(), students.size());
                    Set<String> names = students.stream()
                            .map(Student::getName)
                            .collect(Collectors.toSet());
                    assertTrue(names.contains(sfulgencio.getName()));
                    assertTrue(names.contains(mruiz.getName()));
                })
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStatus(false);
    }

    @Test
    void testGetStudentWithInactiveStatusEmpty() {
        //Given
        given(this.repositoryPort.findByStatus(false)).willReturn(Flux.empty());

        //When and Then
        StepVerifier.create(this.studentService.getStudentsByStatus(false))
                .verifyComplete();
        then(this.repositoryPort).should(times(1)).findByStatus(false);
    }
}
