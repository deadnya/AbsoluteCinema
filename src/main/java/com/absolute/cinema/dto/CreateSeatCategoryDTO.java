package com.absolute.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSeatCategoryDTO(

        @Size(min = 1, max = 128, message = "Name must be between 1 and 128 characters")
        @NotNull(message = "Name cannot be null")
        String name,

        @Min(value = 0, message = "Price must be non-negative")
        Integer priceCents
) {
}
