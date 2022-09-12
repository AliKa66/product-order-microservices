package com.bektas.inventoryservice.repository;

import com.bektas.inventoryservice.dto.InventoryResponse;
import com.bektas.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(String skuCode);


    @Query(value = """
            SELECT new com.bektas.inventoryservice.dto.InventoryResponse(i.skuCode, i.quantity>0 AS isInStock) 
            FROM Inventory i 
            WHERE i.skuCode IN (:skuCodes)""")
    List<InventoryResponse> findBySkuCodeIn(@Param("skuCodes") List<String> skuCodes);
}
