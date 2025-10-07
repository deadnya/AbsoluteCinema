package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.entity.Seat;
import com.absolute.cinema.entity.Session;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.mapper.TicketMapper;
import com.absolute.cinema.repository.SeatRepository;
import com.absolute.cinema.repository.TicketRepository;
import com.absolute.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
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

    @Override
    public List<TicketDTO> getTicketsForSession(UUID sessionId, Ticket.Status status) {
        List<Ticket> tickets;
        if (status != null) {
            tickets = ticketRepository.findBySessionIdAndStatus(sessionId, status);
        } else {
            tickets = ticketRepository.findBySessionId(sessionId);
        }

        return tickets.stream()
                .map(ticketMapper::toDTO)
                .toList();
    }

    @Override
    public TicketDTO reserveTicket(UUID id, User user) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Ticket with id %s not found", id)));

        if (ticket.getStatus() != Ticket.Status.AVAILABLE) {
            throw new BadRequestException(String.format("Ticket with id %s is not available for reservation", id));
        }

        ticket.setStatus(Ticket.Status.RESERVED);
        ticket.setReservedUntil(OffsetDateTime.now().plusMinutes(15));
        ticket.setReservedByUser(user);

        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO cancelReserveForTicket(UUID id, User user) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Ticket with id %s not found", id)));

        if (ticket.getStatus() != Ticket.Status.RESERVED) {
            throw new BadRequestException(String.format("Ticket with id %s is not reserved", id));
        }

        if (ticket.getReservedByUser() == null || !ticket.getReservedByUser().getId().equals(user.getId())) {
            throw new BadRequestException("You can only cancel reservations for tickets you have reserved");
        }

        ticket.setStatus(Ticket.Status.AVAILABLE);
        ticket.setReservedUntil(null);
        ticket.setReservedByUser(null);

        return ticketMapper.toDTO(ticketRepository.save(ticket));
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