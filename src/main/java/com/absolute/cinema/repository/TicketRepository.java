package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    void deleteBySessionId(UUID sessionId);
    List<Ticket> findBySessionId(UUID sessionId);
    List<Ticket> findBySessionIdAndStatus(UUID sessionId, Ticket.Status status);
    boolean existsBySession_Film_IdAndStatusAndPurchase_Client_Id(UUID filmId, Ticket.Status status, UUID userId);
}