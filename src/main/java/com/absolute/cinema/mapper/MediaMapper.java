package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.MediaDTO;
import com.absolute.cinema.entity.Media;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaDTO toDTO(Media media);
}