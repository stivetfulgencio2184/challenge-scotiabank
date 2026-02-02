package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.util;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.StudentRequestV1;
//import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRequestV1;
//import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.v1.request.UserRoleRequestV1;
//import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.RoleEntity;
import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.StudentEntity;
//import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserEntity;
//import org.alpha.omega.student_microservice.infrastructure.adapter.out.persistence.UserRoleEntity;

public class WebFactory {

    public static StudentEntity studentEntityFactory(Integer id, String name, String lastname, Boolean status, Integer age) {
        return new StudentEntity(null, id, name, lastname, status, age);
    }

    public static StudentRequestV1 studentDTOV1Factory(Integer id, String name, String lastname, Boolean status, Integer age) {
        return new StudentRequestV1(id, name, lastname, status, age);
    }

//    public static UserEntity userEntityFactory(Boolean enabled, String username, String password) {
//        return new UserEntity(null, enabled, username, password);
//    }
//
//    public static RoleEntity roleEntityFactory(String name, String abbreviation) {
//        return new RoleEntity(null, name, abbreviation);
//    }
//
//    public static UserRoleEntity userRoleEntityFactory(Integer userId, Integer roleId) {
//        return new UserRoleEntity(userId, roleId);
//    }
//
//    public static UserRequestV1 userRequestV1Factory(Boolean enabled, String username, String password) {
//        return new UserRequestV1(enabled, username, password);
//    }
//
//    public static UserRoleRequestV1 userRoleRequestV1Factory(Integer roleId) {
//        return new UserRoleRequestV1(roleId);
//    }
}
