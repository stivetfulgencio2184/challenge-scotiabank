package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository extends R2dbcRepository<StudentEntity, Integer> {

    @Query(value = "SELECT * FROM students WHERE status=:status")
    Flux<StudentEntity> findByStatus(Boolean status);

    @Query(value = "SELECT * FROM students WHERE id=:id")
    Mono<StudentEntity> findByStudentId(Integer id);
}
