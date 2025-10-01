package com.absolute.cinema.service;

import com.absolute.cinema.dto.PaymentProcessDTO;
import com.absolute.cinema.dto.PaymentResponseDTO;
import com.absolute.cinema.dto.PaymentStatusDTO;
import com.absolute.cinema.entity.User;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentProcessDTO paymentProcessDTO);
    PaymentStatusDTO getPaymentStatus(String paymentId, User user);
}
