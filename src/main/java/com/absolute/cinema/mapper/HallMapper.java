package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.hall.HallCreateRequestDTO;
import com.absolute.cinema.dto.hall.HallDTO;
import com.absolute.cinema.dto.hall.HallListItemDTO;
import com.absolute.cinema.dto.hall.HallUpdateRequestDTO;
import com.absolute.cinema.entity.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HallMapper {

    HallListItemDTO toListItem(Hall entity);

    HallDTO toDTO(Hall entity);

    Hall fromCreate(HallCreateRequestDTO req);

    void updateEntity(@MappingTarget Hall entity, HallUpdateRequestDTO req);
}
