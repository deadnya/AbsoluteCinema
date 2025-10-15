package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.CreateFilmDTO;
import com.absolute.cinema.dto.FilmDTO;
import com.absolute.cinema.entity.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MediaMapper.class})
public interface FilmMapper {
    @Mapping(target = "poster", ignore = true)
    Film toFilm(CreateFilmDTO createFilmDTO);
    
    FilmDTO toDTO(Film film);
}
