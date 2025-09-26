package com.absolute.cinema.service;

import com.absolute.cinema.dto.CreateFilmDTO;
import com.absolute.cinema.dto.FilmDTO;
import com.absolute.cinema.dto.FilmPagedListDTO;
import com.absolute.cinema.dto.UpdateFilmDTO;

import java.util.UUID;

public interface FilmService {
    FilmPagedListDTO getFilms(Integer page, Integer limit);
    FilmDTO createFilm(CreateFilmDTO createFilmDTO);
    FilmDTO getFilmById(UUID id);
    FilmDTO updateFilm(UUID id, UpdateFilmDTO updateFilmDTO);
    void deleteFilm(UUID id);
}
