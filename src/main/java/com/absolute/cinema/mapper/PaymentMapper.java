package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.PaymentResponseDTO;
import com.absolute.cinema.dto.PaymentStatusDTO;
import com.absolute.cinema.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    @Mapping(target = "paymentId", source = "id")
    PaymentStatusDTO toStatusDTO(Payment payment);

    @Mapping(target = "message", source = "message")
    @Mapping(target = "paymentId", source = "payment.id")
    PaymentResponseDTO toResponseDTO(Payment payment, String message);
}