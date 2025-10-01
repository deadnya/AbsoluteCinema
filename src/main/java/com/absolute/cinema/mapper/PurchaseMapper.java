package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.CreatePurchaseDTO;
import com.absolute.cinema.dto.PurchaseDTO;
import com.absolute.cinema.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseMapper {
    PurchaseDTO toPurchaseDTO(Purchase purchase);
    Purchase toEntity(CreatePurchaseDTO createPurchaseDTO);
}