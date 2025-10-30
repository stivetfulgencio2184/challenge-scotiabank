package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.out.StudentRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationTestMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private StudentRepositoryPort repositoryPort;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        this.repositoryPort = mock(StudentRepositoryPort.class);
        this.studentService = new StudentService(repositoryPort);
    }

    @Test
    void testStudentRegistered() {
        //Given
        Student studentRegistered = Student.builder()
                .id(123)
                .name("Jesús")
                .lastName("Salvador")
                .status(Boolean.TRUE)
                .age(33)
                .build();
        given(this.repositoryPort.findByStudentId(123))
                .willReturn(Mono.just(studentRegistered));

        //When and Then
        StepVerifier.create(this.studentService.isStudentRegistered(123))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        verify(this.repositoryPort, times(1)).findByStudentId(123);
    }

    @Test
    void testStudentNotRegistered() {
        //Given
        given(this.repositoryPort.findByStudentId(777))
                .willReturn(Mono.empty());

        //When and Then
        StepVerifier.create(this.studentService.isStudentRegistered(777))
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        verify(this.repositoryPort, times(1)).findByStudentId(777);
    }

    @Test
    void testStudentAlreadyRegistered() {
        //Given
        Student studentRegistered = Student.builder()
                .id(123)
                .name("Jesús")
                .lastName("Salvador")
                .status(Boolean.TRUE)
                .age(33)
                .build();
        given(this.repositoryPort.findByStudentId(123))
                .willReturn(Mono.just(studentRegistered));

        //When and Then
        StepVerifier.create(this.studentService.createStudent(studentRegistered))
                .expectErrorSatisfies(error -> {
                    assert error instanceof AlreadyRegisteredException;
                    assert error.getMessage().equals(String.format(
                            ApplicationTestMessages.STUDENT_ALREADY_REGISTERED, studentRegistered.getId()));
                }).verify();
        verify(this.repositoryPort, times(1)).findByStudentId(123);
    }

    @Test
    void testCreateStudent() {
        //Given
        Student studentToRegister = Student.builder()
                .id(777)
                .name("Pedro")
                .lastName("Winston")
                .status(Boolean.TRUE)
                .age(33)
                .build();
        given(this.repositoryPort.findByStudentId(777))
                .willReturn(Mono.empty());
        given(this.repositoryPort.save(studentToRegister)).willReturn(Mono.just(studentToRegister));

        //When and Then
        StepVerifier.create(this.studentService.createStudent(studentToRegister))
                .expectNextMatches(student -> student.getId().equals(studentToRegister.getId()) &&
                        student.getName().equals(studentToRegister.getName()) &&
                        student.getLastName().equals(studentToRegister.getLastName()) &&
                        student.getStatus().equals(studentToRegister.getStatus()) &&
                        student.getAge().equals(studentToRegister.getAge()))
                .verifyComplete();
        verify(this.repositoryPort, times(1)).findByStudentId(777);
        verify(this.repositoryPort, times(1)).save(studentToRegister);
    }

    @Test
    void testGetAllStudents() {
        //Given
        Student sfulgencio = Student.builder()
                .id(345)
                .name("Stivet")
                .lastName("Fulgencio")
                .status(Boolean.TRUE)
                .age(41)
                .build();
        Student mruiz = Student.builder()
                .id(678)
                .name("Mary")
                .lastName("Ruiz")
                .status(Boolean.FALSE)
                .age(30)
                .build();
        given(this.repositoryPort.findAll()).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.studentService.getAllStudents())
                .expectNext(sfulgencio)
                .expectNext(mruiz)
                .verifyComplete();
        verify(this.repositoryPort, times(1)).findAll();
    }

    @Test
    void testGetStudentByStatus() {
        //Given
        Student sfulgencio = Student.builder()
                .id(345)
                .name("Stivet")
                .lastName("Fulgencio")
                .status(Boolean.TRUE)
                .age(41)
                .build();
        Student mruiz = Student.builder()
                .id(678)
                .name("Mary")
                .lastName("Ruiz")
                .status(Boolean.TRUE)
                .age(30)
                .build();
        given(this.repositoryPort.findByStatus(Boolean.TRUE)).willReturn(Flux.just(sfulgencio, mruiz));

        //When and Then
        StepVerifier.create(this.studentService.getStudentsByStatus(Boolean.TRUE))
                .expectNext(sfulgencio)
                .expectNext(mruiz)
                .verifyComplete();
        verify(this.repositoryPort, times(1)).findByStatus(Boolean.TRUE);
    }
}
