package com.absolute.cinema.dto.hall;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SeatInPlanDTO(
        UUID id,
        Integer row,
        Integer number,
        UUID categoryId,
        String status
) {
}
