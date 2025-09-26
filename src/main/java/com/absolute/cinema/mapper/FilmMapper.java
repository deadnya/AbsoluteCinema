package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.CreateFilmDTO;
import com.absolute.cinema.dto.FilmDTO;
import com.absolute.cinema.entity.Film;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilmMapper {
    Film toFilm(CreateFilmDTO createFilmDTO);
    FilmDTO toDTO(Film film);
}
