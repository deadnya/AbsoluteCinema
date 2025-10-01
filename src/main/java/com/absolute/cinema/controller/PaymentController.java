package com.absolute.cinema.controller;

import com.absolute.cinema.dto.PaymentProcessDTO;
import com.absolute.cinema.dto.PaymentResponseDTO;
import com.absolute.cinema.dto.PaymentStatusDTO;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @RequestBody @Valid PaymentProcessDTO paymentProcessDTO
    ) {
        return ResponseEntity.ok(paymentService.processPayment(paymentProcessDTO));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<PaymentStatusDTO> getPaymentStatus(
            @PathVariable("id") String paymentId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(paymentId, user));
    }
}
