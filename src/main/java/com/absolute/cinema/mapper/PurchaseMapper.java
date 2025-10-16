package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.CreatePurchaseDTO;
import com.absolute.cinema.dto.PurchaseDTO;
import com.absolute.cinema.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseMapper {
    @Mapping(target = "filmId", expression = "java(purchase.getTickets().isEmpty() ? null : purchase.getTickets().get(0).getSession().getFilm().getId())")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "ticketIds", expression = "java(purchase.getTickets().stream().map(ticket -> ticket.getId()).collect(java.util.stream.Collectors.toList()))")
    PurchaseDTO toPurchaseDTO(Purchase purchase);
    Purchase toEntity(CreatePurchaseDTO createPurchaseDTO);
}