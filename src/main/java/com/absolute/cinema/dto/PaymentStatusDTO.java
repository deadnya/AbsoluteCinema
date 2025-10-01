package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Payment;

import java.util.Date;
import java.util.UUID;

public record PaymentStatusDTO(
        UUID paymentId,
        Payment.Status status,
        Date createdAt,
        Date updatedAt
) {
}
