package org.alpha.omega.student_microservice.application.port.out;

import org.alpha.omega.student_microservice.domain.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepositoryPort {

    Mono<Student> save(Student student);
    Mono<Student> findByStudentId(Integer id);
    Flux<Student> findAll();
    Flux<Student> findByStatus(Boolean status);
}
