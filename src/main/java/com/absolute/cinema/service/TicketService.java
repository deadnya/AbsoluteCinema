package com.absolute.cinema.service;

import com.absolute.cinema.entity.Session;
import java.util.UUID;

public interface TicketService {
    void createTicketsForSession(Session session);
    void deleteTicketsBySessionId(UUID sessionId);
}