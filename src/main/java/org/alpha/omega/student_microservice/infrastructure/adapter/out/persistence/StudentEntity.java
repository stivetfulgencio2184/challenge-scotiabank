package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "students")
public record StudentEntity(
        @Id
        Integer pk,
        Integer id,
        String name,
        @Column(value = "last_name")
        String lastName,
        Boolean status,
        Integer age
) {
}
