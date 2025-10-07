package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Film;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateFilmDTO(

        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        @NotNull(message = "Title must not be null")
        String title,

        @Size(min = 1, max = 2000, message = "Description must be between 1 and 2000 characters")
        @NotNull(message = "Description must not be null")
        String description,

        @Min(value = 1, message = "Duration must be at least 1 minute")
        @NotNull(message = "Duration must not be null")
        Integer durationMinutes,

        @NotNull(message = "Age rating must not be null")
        Film.AgeRating ageRating
) {
}
