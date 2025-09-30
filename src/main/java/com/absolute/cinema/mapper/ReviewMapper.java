package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.review.ReviewDTO;
import com.absolute.cinema.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "filmId", source = "film.id")
    @Mapping(target = "clientId", source = "client.id")
    ReviewDTO toDTO(Review entity);
}
