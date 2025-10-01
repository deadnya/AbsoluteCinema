package com.absolute.cinema.dto.hall;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SeatUpsertDTO(
        @NotNull @Min(1)
        Integer row,

        @NotNull @Min(1)
        Integer number,

        @NotNull
        UUID categoryId
) {
}
