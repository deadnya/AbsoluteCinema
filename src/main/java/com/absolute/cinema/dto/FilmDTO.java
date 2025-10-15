package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Film;

import java.util.Date;
import java.util.UUID;

public record FilmDTO(
        UUID id,
        String title,
        String description,
        Integer durationMinutes,
        Film.AgeRating ageRating,
        MediaDTO poster,
        Date createdAt,
        Date updatedAt
) {
}
