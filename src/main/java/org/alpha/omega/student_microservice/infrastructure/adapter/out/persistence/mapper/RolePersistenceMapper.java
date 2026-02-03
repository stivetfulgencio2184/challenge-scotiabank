package org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.mapper;

import org.alpha.omega.student_microservice.domain.model.Role;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

    RolePersistenceMapper INSTANCE = Mappers.getMapper(RolePersistenceMapper.class);

    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role role);
}
