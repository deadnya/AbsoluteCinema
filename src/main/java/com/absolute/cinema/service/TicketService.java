package com.absolute.cinema.service;

import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.entity.Session;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    void createTicketsForSession(Session session);
    void deleteTicketsBySessionId(UUID sessionId);
    List<TicketDTO> getTicketsForSession(UUID sessionId);
    TicketDTO reserveTicket(UUID id);
    TicketDTO cancelReserveForTicket(UUID id);
}