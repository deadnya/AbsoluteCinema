package com.absolute.cinema.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewUpdateDTO(
        @NotNull @Min(0) @Max(5)
        Integer rating,
        @NotNull @Size(max = 2000)
        String text
) {
}
