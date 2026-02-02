package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper;

import org.alpha.omega.student_microservice.domain.model.Student;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.StudentRequestV1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentWebMapperV1 {

    StudentWebMapperV1 INSTANCE = Mappers.getMapper(StudentWebMapperV1.class);

    StudentRequestV1 toDtoV1(Student student);

    Student toDomain(StudentRequestV1 studentRequestV1);
}
