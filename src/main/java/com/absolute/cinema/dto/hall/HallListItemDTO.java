package com.absolute.cinema.dto.hall;


import java.util.UUID;

public record HallListItemDTO(
        UUID id,
        String name,
        Integer number
) {
}
