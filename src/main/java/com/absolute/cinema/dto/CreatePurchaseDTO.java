package com.absolute.cinema.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreatePurchaseDTO(

        @NotEmpty(message = "At least one ticket must be purchased")
        @NotNull(message = "Ticket IDs cannot be null")
        List<UUID> ticketIds
) {
}
