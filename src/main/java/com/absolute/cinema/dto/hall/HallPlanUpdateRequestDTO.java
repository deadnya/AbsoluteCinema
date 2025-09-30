package com.absolute.cinema.dto.hall;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HallPlanUpdateRequestDTO(
        @NotNull @Min(0)
        Integer rows,

        @NotNull
        List<SeatUpsertDTO> seats
        ) {
}
