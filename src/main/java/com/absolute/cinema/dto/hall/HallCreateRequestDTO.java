package com.absolute.cinema.dto.hall;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HallCreateRequestDTO(
        @NotNull(message = "Name cannot be null")
        String name,

        @NotNull(message = "Number cannot be null")
        @Min(value = 1, message = "Number must be >= 1")
        Integer number
) {
}
