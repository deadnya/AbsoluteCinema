package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.*;
import com.absolute.cinema.entity.Film;
import com.absolute.cinema.mapper.FilmMapper;
import com.absolute.cinema.repository.FilmRepository;
import com.absolute.cinema.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    @Override
    public FilmPagedListDTO getFilms(Integer page, Integer limit) {

        if (page < 0) throw new BadRequestException("Page index is less than 0");
        if (limit < 1) throw new BadRequestException("Page size is less than 1");

        Pageable pageable = PageRequest.of(page, limit);

        Page<Film> filmsPage = filmRepository.findAll(pageable);

        List<FilmDTO> filmDTOs = filmsPage.getContent().stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO(
                page,
                limit,
                (int) filmsPage.getTotalElements(),
                filmsPage.getTotalPages()
        );

        return new FilmPagedListDTO(filmDTOs, pageDTO);
    }

    @Override
    public FilmDTO createFilm(CreateFilmDTO createFilmDTO) {
        return filmMapper.toDTO(filmRepository.save(filmMapper.toFilm(createFilmDTO)));
    }

    @Override
    public FilmDTO getFilmById(UUID id) {

        Film film = filmRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Film with id: %s not found", id))
        );

        return filmMapper.toDTO(film);
    }

    @Override
    public FilmDTO updateFilm(UUID id, UpdateFilmDTO updateFilmDTO) {

        Film film = filmRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Film with id: %s not found", id))
        );

        film.setTitle(updateFilmDTO.title());
        film.setDescription(updateFilmDTO.description());
        film.setDurationMinutes(updateFilmDTO.durationMinutes());
        film.setAgeRating(updateFilmDTO.ageRating());

        return filmMapper.toDTO(filmRepository.save(film));
    }

    @Override
    public void deleteFilm(UUID id) {

        if (!filmRepository.existsById(id)) {
            throw new NotFoundException(String.format("Film with id: %s not found", id));
        }

        filmRepository.deleteById(id);
    }
}
