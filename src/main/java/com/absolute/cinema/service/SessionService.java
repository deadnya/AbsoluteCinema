package com.absolute.cinema.service;

import com.absolute.cinema.dto.CreateSessionDTO;
import com.absolute.cinema.dto.EditSessionDTO;
import com.absolute.cinema.dto.SessionDTO;
import com.absolute.cinema.dto.SessionPagedListDTO;

import java.util.Date;
import java.util.UUID;

public interface SessionService {
    SessionPagedListDTO getSessions(int page, int size, UUID filmId, Date date);
    SessionDTO createSession(CreateSessionDTO createSessionDTO);
    SessionDTO getSession(UUID id);
    SessionDTO editSession(UUID id, EditSessionDTO editSessionDTO);
    void deleteSession(UUID id);
}
