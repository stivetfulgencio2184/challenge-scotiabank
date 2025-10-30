package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper;

import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentPersistenceMapper {

    StudentPersistenceMapper INSTANCE = Mappers.getMapper(StudentPersistenceMapper.class);

    Student toDomain(StudentEntity entity);

    StudentEntity toEntity(Student student);
}
