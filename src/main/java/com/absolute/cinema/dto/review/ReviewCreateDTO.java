package com.absolute.cinema.dto.review;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

public record ReviewCreateDTO(
        @NotNull
        @Range(min = 1, max = 5, message = "Rating must be between 1 and 5")
        Integer rating,

        @NotNull @Size(max = 2000, message = "Text must be at most 2000 characters")
        String text
) {
}
