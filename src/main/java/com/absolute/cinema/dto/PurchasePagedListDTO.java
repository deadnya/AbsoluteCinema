package com.absolute.cinema.dto;

import java.util.List;

public record PurchasePagedListDTO(
        List<PurchaseDTO> data,
        PageDTO pagination
) {
}
