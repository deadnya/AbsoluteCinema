package com.absolute.cinema.controller;

import com.absolute.cinema.dto.CreateSessionDTO;
import com.absolute.cinema.dto.EditSessionDTO;
import com.absolute.cinema.dto.SessionDTO;
import com.absolute.cinema.dto.SessionPagedListDTO;
import com.absolute.cinema.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
@CrossOrigin
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<SessionPagedListDTO> getSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UUID filmId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
        return ResponseEntity.ok(sessionService.getSessions(page, size, filmId, date));
    }

    @PostMapping
    public ResponseEntity<SessionDTO> createSession(
            @RequestBody @Valid CreateSessionDTO createSessionDTO
    ) {
        return ResponseEntity.ok(sessionService.createSession(createSessionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSession(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(sessionService.getSession(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> editSession(
            @PathVariable UUID id,
            @RequestBody @Valid EditSessionDTO editSessionDTO
    ) {
        return ResponseEntity.ok(sessionService.editSession(id, editSessionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable UUID id
    ) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }
}
