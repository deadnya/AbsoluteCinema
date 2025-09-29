package com.absolute.cinema.dto;

import java.util.Date;

public record TimeslotDTO(
        Date start,
        Date end
) {
}
