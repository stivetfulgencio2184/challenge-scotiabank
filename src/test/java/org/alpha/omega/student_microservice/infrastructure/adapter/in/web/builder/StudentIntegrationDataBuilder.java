package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.builder;

import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util.WebFactory.studentEntityFactory;

@Component
public class StudentIntegrationDataBuilder {

    private final StudentRepository studentRepository;

    public StudentIntegrationDataBuilder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Mono<Void> build() {
        return cleanStudentTable()
                .then(loadStudents());
    }

    private Mono<Void> cleanStudentTable() {
        return this.studentRepository.deleteAll();
    }

    private Mono<Void> loadStudents() {
        return this.studentRepository.saveAll(List.of(
                studentEntityFactory(777, "Jesús", "Salvador", true, 33),
                studentEntityFactory(246, "Stivet", "Fulgencio", false, 41),
                studentEntityFactory(864, "Mary", "Ruiz", true, 30)))
                .then();
    }
}
