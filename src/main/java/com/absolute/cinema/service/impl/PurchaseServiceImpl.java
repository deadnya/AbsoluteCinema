package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.ForbiddenException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.CreatePurchaseDTO;
import com.absolute.cinema.dto.PageDTO;
import com.absolute.cinema.dto.PurchaseDTO;
import com.absolute.cinema.dto.PurchasePagedListDTO;
import com.absolute.cinema.entity.Purchase;
import com.absolute.cinema.entity.Ticket;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.mapper.PurchaseMapper;
import com.absolute.cinema.repository.PurchaseRepository;
import com.absolute.cinema.repository.TicketRepository;
import com.absolute.cinema.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;
    private final PurchaseMapper purchaseMapper;

    @Override
    public PurchasePagedListDTO getPurchasesForClient(int page, int size, UUID clientId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Purchase> purchasePage = purchaseRepository.findByClientId(clientId, pageable);

        List<PurchaseDTO> purchaseDTOs = purchasePage.getContent().stream()
                .map(purchaseMapper::toPurchaseDTO)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO(
                page,
                size,
                (int) purchasePage.getTotalElements(),
                purchasePage.getTotalPages()
        );

        return new PurchasePagedListDTO(purchaseDTOs, pageDTO);
    }

    @Override
    @Transactional
    public PurchaseDTO createPurchaseForClient(CreatePurchaseDTO createPurchaseDTO, User user) {
        Purchase purchase = new Purchase();

        int priceCents = 0;

        for (var ticketId : createPurchaseDTO.ticketIds()) {
            Ticket existingTicket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new NotFoundException(String.format("Ticket with id %s not found", ticketId)));

            if (existingTicket.getStatus() != Ticket.Status.RESERVED ||
                existingTicket.getReservedByUser() == null ||
                !existingTicket.getReservedByUser().getId().equals(user.getId())) {
                throw new BadRequestException(String.format(
                        "Ticket with id %s is not reserved by user %s", ticketId, user.getId())
                );
            }

            existingTicket.setPurchase(purchase);
            ticketRepository.save(existingTicket);

            priceCents += existingTicket.getPriceCents();
        }

        purchase.setClient(user);
        purchase.setStatus(Purchase.Status.PENDING);
        purchase.setTotalCents(priceCents);

        Purchase savedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseDTO(savedPurchase);
    }

    @Override
    public PurchaseDTO getPurchaseById(UUID purchaseId, User user) {
        Purchase purchase = getPurchaseAndValidateAccess(purchaseId, user);
        return purchaseMapper.toPurchaseDTO(purchase);
    }

    @Override
    @Transactional
    public PurchaseDTO cancelPurchaseById(UUID purchaseId, User user) {
        Purchase purchase = getPurchaseAndValidateAccess(purchaseId, user);

        if (purchase.getStatus() != Purchase.Status.PENDING &&
                purchase.getStatus() != Purchase.Status.PAID) {
            throw new BadRequestException(String.format(
                    "Only PENDING or PAID purchases can be cancelled. Current status: %s",
                    purchase.getStatus())
            );
        }

        for (Ticket ticket : purchase.getTickets()) {
            ticket.setStatus(Ticket.Status.AVAILABLE);
            ticket.setPurchase(null);
            ticket.setReservedByUser(null);
            ticket.setReservedUntil(null);
            ticketRepository.save(ticket);
        }

        purchase.setStatus(Purchase.Status.CANCELLED);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseDTO(savedPurchase);
    }

    private Purchase getPurchaseAndValidateAccess(UUID purchaseId, User user) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with id %s not found", purchaseId)));

        if (!isUserAuthorized(purchase, user)) {
            throw new ForbiddenException(String.format(
                    "User %s is not authorized to access purchase %s", user.getId(), purchaseId)
            );
        }

        return purchase;
    }

    private boolean isUserAuthorized(Purchase purchase, User user) {
        return purchase.getClient().getId().equals(user.getId()) ||
                user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}