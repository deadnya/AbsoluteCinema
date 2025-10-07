package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.ForbiddenException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.PaymentProcessDTO;
import com.absolute.cinema.dto.PaymentResponseDTO;
import com.absolute.cinema.dto.PaymentStatusDTO;
import com.absolute.cinema.entity.Payment;
import com.absolute.cinema.entity.Purchase;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.mapper.PaymentMapper;
import com.absolute.cinema.repository.PaymentRepository;
import com.absolute.cinema.repository.PurchaseRepository;
import com.absolute.cinema.repository.TicketRepository;
import com.absolute.cinema.service.EmailSenderService;
import com.absolute.cinema.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final PurchaseRepository purchaseRepository;
    private final PaymentMapper paymentMapper;
    private final EmailSenderService emailSenderService;
    private final Random random = new Random();

    @Override
    public PaymentResponseDTO processPayment(PaymentProcessDTO paymentProcessDTO) {

        Purchase purchase = purchaseRepository.findById(paymentProcessDTO.purchaseId())
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with id %s not found", paymentProcessDTO.purchaseId())));

        Payment payment = new Payment();
        payment.setPurchase(purchase);

        Payment.Status randomStatus = getRandomPaymentStatus();
        payment.setStatus(randomStatus);

        Payment savedPayment = paymentRepository.save(payment);

        updatePurchaseStatus(purchase, Payment.Status.SUCCESS);
        purchaseRepository.save(purchase);

        purchase.getPayments().add(savedPayment);

        String message = switch (randomStatus) {
            case SUCCESS -> "Payment processed successfully";
            case FAILED -> "Payment failed";
            case PENDING -> "Payment is being processed";
        };

        for (var ticket : purchase.getTickets()) {
            ticket.setStatus(Ticket.Status.SOLD);
            ticketRepository.save(ticket);
        }

        emailSenderService.sendEmail(
                purchase.getClient().getEmail(),
                "Payment Status",
                String.format("Your payment for purchase ID %s is %s.", purchase.getId(), message)
        );

        return paymentMapper.toResponseDTO(savedPayment, message);
    }

    @Override
    public PaymentStatusDTO getPaymentStatus(String paymentId, User user) {
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id %s not found", paymentId)));

        if (!isUserAuthorized(payment.getPurchase(), user)) {
            throw new ForbiddenException(String.format("User with id %s is not authorized to view this payment", user.getId()));
        }

        return paymentMapper.toStatusDTO(payment);
    }

    private Payment.Status getRandomPaymentStatus() {
        Payment.Status[] statuses = Payment.Status.values();
        return statuses[random.nextInt(statuses.length)];
    }

    private void updatePurchaseStatus(Purchase purchase, Payment.Status paymentStatus) {
        switch (paymentStatus) {
            case SUCCESS -> purchase.setStatus(Purchase.Status.PAID);
            case FAILED -> purchase.setStatus(Purchase.Status.FAILED);
            case PENDING -> purchase.setStatus(Purchase.Status.PENDING);
        }
    }

    private boolean isUserAuthorized(Purchase purchase, User user) {
        return purchase.getClient().getId().equals(user.getId()) ||
                user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}