package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Purchase;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record PurchaseDTO(
        UUID id,
        UUID clientId,
        UUID filmId,
        List<UUID> ticketIds,
        Integer totalCents,
        Purchase.Status status,
        Date createdAt
) {
}
