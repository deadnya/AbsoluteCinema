package com.absolute.cinema.dto.hall;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HallPlanUpdateRequestDTO(
        @NotNull(message = "Rows cannot be null")
        @Min(value = 0, message = "Rows must be at least 0")
        Integer rows,

        @NotNull(message = "Seats cannot be null")
        @Valid
        List<SeatUpsertDTO> seats
        ) {
}
