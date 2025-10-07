package com.absolute.cinema.dto.hall;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SeatUpsertDTO(
        @NotNull(message = "Row must not be null")
        @Min(value = 1, message = "Row must be at least 1")
        Integer row,

        @NotNull(message = "Seat number must not be null")
        @Min(value = 1, message = "Seat number must be at least 1")
        Integer number,

        @NotNull(message = "Category ID must not be null")
        UUID categoryId
) {
}
