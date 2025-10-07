package com.absolute.cinema.dto.hall;

import com.absolute.cinema.dto.SeatCategoryDTO;

import java.util.List;
import java.util.UUID;

public record HallPlanDTO (
        UUID hallId,
        Integer rows,
        List<SeatInPlanDTO> seats,
        List<SeatCategoryDTO> categories
) {
}
