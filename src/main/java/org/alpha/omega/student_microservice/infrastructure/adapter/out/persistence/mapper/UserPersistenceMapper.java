package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper;

import org.alpha.omega.student_microservice.domain.model.User;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    UserPersistenceMapper INSTANCE = Mappers.getMapper(UserPersistenceMapper.class);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User user);
}
