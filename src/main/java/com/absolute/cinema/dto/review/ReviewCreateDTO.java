package com.absolute.cinema.dto.review;

import jakarta.validation.constraints.*;

public record ReviewCreateDTO(
        @NotNull @Min(0) @Max(5)
        Integer rating,

        @NotNull @Size(max = 2000)
        String text
) {
}
