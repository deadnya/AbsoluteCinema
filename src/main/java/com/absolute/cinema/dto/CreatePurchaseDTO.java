package com.absolute.cinema.dto;

import java.util.List;
import java.util.UUID;

public record CreatePurchaseDTO(
        List<UUID> ticketIds
) {
}
