package com.absolute.cinema.dto;

public record PageDTO(
        Integer page,
        Integer limit,
        Integer total,
        Integer pages
) {
}
