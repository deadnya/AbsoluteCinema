package com.absolute.cinema.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(

        @NotNull(message = "Email cannot be null")
        String email,

        @NotNull(message = "Password cannot be null")
        String password
) {
}
