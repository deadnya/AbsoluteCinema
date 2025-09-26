package com.absolute.cinema.dto.hall;

import java.util.Date;
import java.util.UUID;

public record HallDTO(
        UUID id,
        String name,
        Integer number,
        Date createdAt,
        Date updatedAt
) {
}
