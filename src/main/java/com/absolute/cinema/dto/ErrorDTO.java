package com.absolute.cinema.dto;

import java.util.List;

public record ErrorDTO(
        Integer statusCode,
        List<String> errors
) { }