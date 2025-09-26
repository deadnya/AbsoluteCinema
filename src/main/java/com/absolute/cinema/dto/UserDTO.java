package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Role;
import com.absolute.cinema.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
    UUID id,
    String email,
    String firstName,
    String lastName,
    Integer age,
    Role.RoleType roleType,
    User.Gender gender,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
