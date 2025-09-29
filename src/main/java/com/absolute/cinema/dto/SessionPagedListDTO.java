package com.absolute.cinema.dto;

import java.util.List;

public record SessionPagedListDTO(
        List<SessionDTO> data,
        PageDTO pagination
) {
}
