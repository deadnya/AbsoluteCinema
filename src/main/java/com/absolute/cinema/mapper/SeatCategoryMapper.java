package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.CreateSeatCategoryDTO;
import com.absolute.cinema.dto.SeatCategoryDTO;
import com.absolute.cinema.entity.SeatCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeatCategoryMapper {
    SeatCategory toSeatCategory(CreateSeatCategoryDTO createSeatCategoryDTO);
    SeatCategoryDTO toDTO(SeatCategory seatCategory);
}
