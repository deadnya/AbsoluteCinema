package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.UserDTO;
import com.absolute.cinema.entity.Role;
import com.absolute.cinema.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleType", expression = "java(getRoleTypeFromRoles(user.getRoles()))")
    UserDTO toDTO(User user);

    default Role.RoleType getRoleTypeFromRoles(Set<Role> roles) {
        return roles.isEmpty() ? null : roles.iterator().next().getRole();
    }
}