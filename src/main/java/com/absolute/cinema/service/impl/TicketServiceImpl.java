package com.absolute.cinema.service.impl;

import com.absolute.cinema.entity.Seat;
import com.absolute.cinema.entity.Session;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.repository.SeatRepository;
import com.absolute.cinema.repository.TicketRepository;
import com.absolute.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Override
    public void createTicketsForSession(Session session) {
        List<Seat> seats = seatRepository.findByHallId(session.getHall().getId());

        List<Ticket> tickets = seats.stream()
                .map(seat -> createTicket(session, seat))
                .toList();

        ticketRepository.saveAll(tickets);
    }

    @Override
    public void deleteTicketsBySessionId(UUID sessionId) {
        ticketRepository.deleteBySessionId(sessionId);
    }

    private Ticket createTicket(Session session, Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setSession(session);
        ticket.setSeat(seat);
        ticket.setCategory(seat.getCategory());
        ticket.setPriceCents(seat.getCategory().getPriceCents());
        ticket.setStatus(Ticket.Status.AVAILABLE);
        return ticket;
    }
}