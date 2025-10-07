package com.absolute.cinema.controller;

import com.absolute.cinema.dto.CreateFilmDTO;
import com.absolute.cinema.dto.FilmDTO;
import com.absolute.cinema.dto.FilmPagedListDTO;
import com.absolute.cinema.dto.UpdateFilmDTO;
import com.absolute.cinema.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@CrossOrigin
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<FilmPagedListDTO> getAllFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(filmService.getFilms(page, size));
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(
            @RequestBody @Valid CreateFilmDTO createFilmDTO
    ) {
        return ResponseEntity.ok(filmService.createFilm(createFilmDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmDTO> updateFilm(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateFilmDTO updateFilmDTO
    ) {
        return ResponseEntity.ok(filmService.updateFilm(id, updateFilmDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(
            @PathVariable UUID id
    ) {
        filmService.deleteFilm(id);
        return ResponseEntity.ok().build();
    }
}
