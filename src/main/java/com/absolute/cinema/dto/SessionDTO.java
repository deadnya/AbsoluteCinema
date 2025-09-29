package com.absolute.cinema.dto;

import java.util.Date;
import java.util.UUID;

public record SessionDTO(
        UUID id,
        UUID filmId,
        UUID hallId,
        Date startAt,
        TimeslotDTO timeslot
) {
}
