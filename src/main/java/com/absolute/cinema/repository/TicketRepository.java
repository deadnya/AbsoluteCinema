package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    void deleteBySessionId(UUID sessionId);
}
