package com.absolute.cinema.dto;

import com.absolute.cinema.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(

        @Email(message = "Email should be valid")
        @NotNull(message = "Email cannot be null")
        String email,

        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @NotNull(message = "First name cannot be null")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @NotNull(message = "Last name cannot be null")
        String lastName,

        @NotNull(message = "Age cannot be null")
        Integer age,

        @NotNull(message = "Gender cannot be null")
        User.Gender gender
) {
}
