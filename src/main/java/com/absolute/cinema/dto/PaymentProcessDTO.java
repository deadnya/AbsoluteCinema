package com.absolute.cinema.dto;

import java.util.UUID;

public record PaymentProcessDTO(
        UUID purchaseId,
        String cardNumber,
        String expiryDate,
        String cvv,
        String cardHolderName
) {
}
