package com.absolute.cinema.controller;

import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/sessions/{sessionId}/tickets")
    public ResponseEntity<List<TicketDTO>> getTicketsForSession(
            @PathVariable UUID sessionId,
            @RequestParam(required = false) Ticket.Status status
    ) {
        return ResponseEntity.ok(ticketService.getTicketsForSession(sessionId, status));
    }

    @PostMapping("/tickets/{id}/reserve")
    public ResponseEntity<TicketDTO> reserveTicket(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(ticketService.reserveTicket(id, user));
    }

    @PostMapping("/tickets/{id}/cancel-reservation")
    public ResponseEntity<TicketDTO> cancelReservation(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(ticketService.cancelReserveForTicket(id, user));
    }
}
