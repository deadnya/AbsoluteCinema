package com.absolute.cinema.dto.review;

import com.absolute.cinema.dto.PageDTO;

import java.util.List;

public record ReviewPagedListDTO(
        List<ReviewDTO> data,
        PageDTO pagination
) {
}
