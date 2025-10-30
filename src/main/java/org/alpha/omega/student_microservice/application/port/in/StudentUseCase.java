package org.alpha.omega.student_microservice.application.port.in;

import org.alpha.omega.student_microservice.domain.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentUseCase {

    Mono<Boolean> isStudentRegistered(Integer id);
    Mono<Student> createStudent(Student student);
    Flux<Student> getAllStudents();
    Flux<Student> getStudentsByStatus(Boolean status);
}
