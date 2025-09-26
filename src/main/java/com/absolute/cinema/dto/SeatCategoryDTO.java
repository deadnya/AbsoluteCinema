package com.absolute.cinema.dto;

import java.util.UUID;

public record SeatCategoryDTO(
        UUID id,
        String name,
        Integer priceCents
) { }
