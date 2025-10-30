package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.alpha.omega.student_microservice.application.port.out.StudentRepositoryPort;
import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper.StudentPersistenceMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final StudentRepository repository;
    private final StudentPersistenceMapper mapper;

    public StudentRepositoryAdapter(StudentRepository repository, StudentPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Student> save(Student student) {
        return this.repository
                .save(StudentPersistenceMapper.INSTANCE.toEntity(student))
                .map(this.mapper::toDomain);
    }

    @Override
    public Mono<Student> findByStudentId(Integer id) {
        return this.repository.findByStudentId(id)
                .map(this.mapper::toDomain);
    }

    @Override
    public Flux<Student> findAll() {
        return this.repository.findAll()
                .map(this.mapper::toDomain);
    }

    @Override
    public Flux<Student> findByStatus(Boolean status) {
        return this.repository.findByStatus(status)
                .map(this.mapper::toDomain);
    }
}
