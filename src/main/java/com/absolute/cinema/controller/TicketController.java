package com.absolute.cinema.controller;

import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.service.TicketService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @PathVariable @Size(min = 36, max = 36) UUID sessionId
    ) {
        return ResponseEntity.ok(ticketService.getTicketsForSession(sessionId));
    }

    @PostMapping("/tickets/{id}/reserve")
    public ResponseEntity<TicketDTO> reserveTicket(
            @PathVariable @Size(min = 36, max = 36) UUID id
    ) {
        return ResponseEntity.ok(ticketService.reserveTicket(id));
    }

    @PostMapping("/tickets/{id}/cancel-reservation")
    public ResponseEntity<TicketDTO> cancelReservation(
            @PathVariable @Size(min = 36, max = 36) UUID id
    ) {
        return ResponseEntity.ok(ticketService.cancelReserveForTicket(id));
    }
}
