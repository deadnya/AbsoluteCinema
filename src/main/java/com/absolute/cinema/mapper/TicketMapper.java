package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.TicketDTO;
import com.absolute.cinema.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {
    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "seat.id", target = "seatId")
    @Mapping(source = "category.id", target = "categoryId")
    TicketDTO toDTO(Ticket ticket);
}