package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.*;
import com.absolute.cinema.entity.Film;
import com.absolute.cinema.entity.Hall;
import com.absolute.cinema.entity.Session;
import com.absolute.cinema.mapper.SessionMapper;
import com.absolute.cinema.repository.FilmRepository;
import com.absolute.cinema.repository.HallRepository;
import com.absolute.cinema.repository.SessionRepository;
import com.absolute.cinema.service.SessionService;
import com.absolute.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;
    private final TicketService ticketService;
    private final SessionMapper sessionMapper;

    @Override
    public SessionPagedListDTO getSessions(int page, int size, UUID filmId, Date date) {

        if (page < 0) throw new BadRequestException("Page index must not be less than zero");
        if (size < 1) throw new BadRequestException("Page size must not be less than one");

        Pageable pageable = PageRequest.of(page, size);
        Page<Session> sessions;

        sessions = sessionRepository.findAll(pageable);

        return new SessionPagedListDTO(
                sessions.getContent().stream()
                        .map(sessionMapper::toDTO)
                        .toList(),
                new PageDTO(
                        page,
                        size,
                        (int) sessions.getTotalElements(),
                        sessions.getTotalPages()
                )
        );
    }

    @Override
    public SessionDTO createSession(CreateSessionDTO dto) {
        Film film = filmRepository.findById(dto.filmId()).orElseThrow(
                () -> new NotFoundException(String.format("Film with id %s not found", dto.filmId()))
        );

        Hall hall = hallRepository.findById(dto.hallId()).orElseThrow(
                () -> new NotFoundException(String.format("Hall with id %s not found", dto.hallId()))
        );

        OffsetDateTime startAt = OffsetDateTime.ofInstant(
                dto.startAt().toInstant(),
                ZoneId.systemDefault()
        );

        validateSessionTimeSlot(hall.getId(), null, startAt, film.getDurationMinutes());

        Session session = new Session();
        session.setFilm(film);
        session.setHall(hall);
        session.setStartAt(startAt);

        session = sessionRepository.save(session);

        ticketService.createTicketsForSession(session);

        return sessionMapper.toDTO(session);
    }

    @Override
    public SessionDTO getSession(UUID id) {
        Session session = sessionRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Session with id %s not found", id)));

        return sessionMapper.toDTO(session);
    }

    @Override
    public SessionDTO editSession(UUID id, EditSessionDTO dto) {
        Session session = sessionRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Session with id %s not found", id))
        );

        Film film = filmRepository.findById(dto.filmId()).orElseThrow(
                () -> new NotFoundException(String.format("Film with id %s not found", dto.filmId()))
        );

        Hall hall = hallRepository.findById(dto.hallId()).orElseThrow(
                () -> new NotFoundException(String.format("Hall with id %s not found", dto.hallId()))
        );

        OffsetDateTime startAt = OffsetDateTime.ofInstant(
                dto.startAt().toInstant(),
                ZoneId.systemDefault()
        );

        validateSessionTimeSlot(hall.getId(), id, startAt, film.getDurationMinutes());

        session.setFilm(film);
        session.setHall(hall);
        session.setStartAt(startAt);

        session = sessionRepository.save(session);
        return sessionMapper.toDTO(session);
    }

    @Override
    public void deleteSession(UUID id) {
        if (!sessionRepository.existsById(id)) {
            throw new NotFoundException(String.format("Session with id %s not found", id));
        }

        ticketService.deleteTicketsBySessionId(id);

        sessionRepository.deleteById(id);
    }

    private void validateSessionTimeSlot(UUID hallId, UUID sessionId, OffsetDateTime startTime, int durationMinutes) {
        OffsetDateTime endTime = startTime.plusMinutes(durationMinutes);

        List<Session> conflictingSessions = sessionRepository.findByHallIdAndStartAtBetween(
                hallId,
                startTime.minusMinutes(durationMinutes + 20),
                endTime.plusMinutes(20)
        );

        if (sessionId != null) {
            conflictingSessions = conflictingSessions.stream()
                    .filter(session -> !session.getId().equals(sessionId))
                    .toList();
        }

        for (Session other : conflictingSessions) {
            OffsetDateTime otherStart = other.getStartAt();
            OffsetDateTime otherEnd = otherStart.plusMinutes(other.getFilm().getDurationMinutes());

            if (startTime.isAfter(otherEnd) &&
                    ChronoUnit.MINUTES.between(otherEnd, startTime) < 20) {
                throw new BadRequestException("Session must start at least 20 minutes after the previous session ends");
            }

            if (endTime.isBefore(otherStart) &&
                    ChronoUnit.MINUTES.between(endTime, otherStart) < 20) {
                throw new BadRequestException("Session must end at least 20 minutes before the next session starts");
            }

            if ((startTime.isBefore(otherEnd) && endTime.isAfter(otherStart))) {
                throw new BadRequestException("Session overlaps with an existing session");
            }
        }
    }
}