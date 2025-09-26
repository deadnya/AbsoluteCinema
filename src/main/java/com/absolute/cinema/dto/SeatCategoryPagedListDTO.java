package com.absolute.cinema.dto;

import java.util.List;

public record SeatCategoryPagedListDTO(
        List<SeatCategoryDTO> data,
        PageDTO pagination
) { }
