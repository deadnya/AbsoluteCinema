package com.absolute.cinema.dto.review;

import java.util.Date;
import java.util.UUID;

public record ReviewDTO(
        UUID id,
        UUID filmId,
        UUID clientId,
        Integer rating,
        String text,
        Date createdAt
) {
}
