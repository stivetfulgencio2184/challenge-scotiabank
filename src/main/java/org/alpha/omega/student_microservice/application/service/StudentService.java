package org.alpha.omega.student_microservice.application.service;

import org.alpha.omega.student_microservice.application.port.in.StudentUseCase;
import org.alpha.omega.student_microservice.application.port.out.StudentRepositoryPort;
import org.alpha.omega.student_microservice.application.util.ApplicationMessages;
import org.alpha.omega.student_microservice.domain.exception.AlreadyRegisteredException;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService implements StudentUseCase {

    private final StudentRepositoryPort repositoryPort;

    public StudentService(StudentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Mono<Boolean> isStudentRegistered(Integer id) {
        return this.repositoryPort.findByStudentId(id)
                .map(student -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Student> createStudent(Student student) {
        return isStudentRegistered(student.getId())
                .flatMap(exists -> {
                    if(Boolean.TRUE.equals(exists))
                        return Mono.error(new AlreadyRegisteredException(
                                String.format(ApplicationMessages.STUDENT_ALREADY_REGISTERED, student.getId())));
                    else
                        return this.repositoryPort.save(student);
                });
    }

    @Override
    public Flux<Student> getAllStudents() {
        return this.repositoryPort.findAll();
    }

    @Override
    public Flux<Student> getStudentsByStatus(Boolean status) {
        return this.repositoryPort.findByStatus(status);
    }
}
