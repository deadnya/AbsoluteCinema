package com.absolute.cinema.service;

import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.entity.Session;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.entity.User;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    void createTicketsForSession(Session session);
    void deleteTicketsBySessionId(UUID sessionId);
    List<TicketDTO> getTicketsForSession(UUID sessionId, Ticket.Status status);
    TicketDTO reserveTicket(UUID id, User user);
    TicketDTO cancelReserveForTicket(UUID id, User user);
}