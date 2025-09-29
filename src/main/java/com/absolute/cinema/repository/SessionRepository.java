package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByHallIdAndStartAtBetween(UUID hallId, OffsetDateTime start, OffsetDateTime end);
}