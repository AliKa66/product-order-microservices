package com.bektas.inventoryservice.service;

import com.bektas.inventoryservice.dto.InventoryResponse;
import com.bektas.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryBySkuCodes(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes);
    }
}
