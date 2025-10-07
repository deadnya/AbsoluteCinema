package com.absolute.cinema.dto.hall;

import com.absolute.cinema.dto.PageDTO;

import java.util.List;

public record HallPagedListDTO(
        List<HallDTO> data,
        PageDTO pagination
) {
}
