package com.bektas.inventoryservice.service;

import com.bektas.inventoryservice.dto.InventoryResponse;
import com.bektas.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows // for the exception of Thread.sleep. Don't use it in production
    public List<InventoryResponse> getInventoryBySkuCodes(List<String> skuCodes) {
        log.info("LONG PROCESS STARTED");
        Thread.sleep(10000);
        log.info("LONG PROCESS ENDED");
        return inventoryRepository.findBySkuCodeIn(skuCodes);
    }
}
