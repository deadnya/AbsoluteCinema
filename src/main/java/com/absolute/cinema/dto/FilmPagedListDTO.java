package com.absolute.cinema.dto;

import java.util.List;

public record FilmPagedListDTO(
        List<FilmDTO> data,
        PageDTO pagination
) {
}
