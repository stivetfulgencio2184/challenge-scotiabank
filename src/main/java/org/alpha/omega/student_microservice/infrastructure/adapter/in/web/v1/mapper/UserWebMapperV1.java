package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.mapper;

import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRequestV1;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.response.UserResponseV1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = RoleWebMapperV1.class)
public interface UserWebMapperV1 {

    UserWebMapperV1 INSTANCE = Mappers.getMapper(UserWebMapperV1.class);

    UserResponseV1 toResponseV1(User user);

    User toDomain(UserRequestV1 requestV1);

}
