package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.RoleRequestV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response.RoleResponseV1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface RoleWebMapperV1 {

    RoleWebMapperV1 INSTANCE = Mappers.getMapper(RoleWebMapperV1.class);

    RoleResponseV1 toResponseV1(Role role);

    Role toDomain(RoleRequestV1 requestV1);
}
