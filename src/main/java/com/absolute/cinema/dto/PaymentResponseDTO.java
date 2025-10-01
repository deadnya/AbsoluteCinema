package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Payment;

import java.util.UUID;

public record PaymentResponseDTO(
        UUID paymentId,
        Payment.Status status,
        String message
) {
}
