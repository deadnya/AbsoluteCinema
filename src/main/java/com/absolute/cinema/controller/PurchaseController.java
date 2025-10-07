package com.absolute.cinema.controller;

import com.absolute.cinema.dto.CreatePurchaseDTO;
import com.absolute.cinema.dto.PurchaseDTO;
import com.absolute.cinema.dto.PurchasePagedListDTO;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@CrossOrigin
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<PurchasePagedListDTO> getAllMyPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(purchaseService.getPurchasesForClient(page, size, user.getId()));
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(
            @RequestBody @Valid CreatePurchaseDTO createPurchaseDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(purchaseService.createPurchaseForClient(createPurchaseDTO, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id, user));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseDTO> cancelPurchaseById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(purchaseService.cancelPurchaseById(id, user));
    }
}
