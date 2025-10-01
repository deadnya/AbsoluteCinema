package com.absolute.cinema.service;

import com.absolute.cinema.dto.CreatePurchaseDTO;
import com.absolute.cinema.dto.PurchaseDTO;
import com.absolute.cinema.dto.PurchasePagedListDTO;
import com.absolute.cinema.entity.User;

import java.util.UUID;

public interface PurchaseService {
    PurchasePagedListDTO getPurchasesForClient(int page, int size, UUID clientId);
    PurchaseDTO createPurchaseForClient(CreatePurchaseDTO createPurchaseDTO, User user);
    PurchaseDTO getPurchaseById(UUID purchaseId, User user);
    PurchaseDTO cancelPurchaseById(UUID purchaseId, User user);
}
