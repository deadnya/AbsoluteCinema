package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Ticket;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TicketDTO(
        UUID id,
        UUID sessionId,
        UUID seatId,
        UUID categoryId,
        Integer priceCents,
        Ticket.Status status,
        OffsetDateTime reservedUntil
) {
}
