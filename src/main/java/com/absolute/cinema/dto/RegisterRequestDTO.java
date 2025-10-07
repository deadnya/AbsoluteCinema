package com.absolute.cinema.dto;

import com.absolute.cinema.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @Email(message = "Email should be valid")
        @NotNull(message = "Email cannot be null")
        String email,

        @NotNull(message = "Password cannot be null")
        String password,

        @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        @NotNull(message = "First name cannot be null")
        String firstName,

        @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
        @NotNull(message = "Last name cannot be null")
        String lastName,

        @Min(value = 0, message = "Age must be non-negative")
        @NotNull(message = "Age cannot be null")
        Integer age,

        @NotNull(message = "Gender cannot be null")
        User.Gender gender
) {
}
