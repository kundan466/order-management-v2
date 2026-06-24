package com.order_management.demo.mapper;

import com.order_management.demo.dto.InventoryRequestDTO;
import com.order_management.demo.dto.InventoryResponseDTO;
import com.order_management.demo.entity.Inventory;

public class InventoryMapper {

    public static Inventory toEntity(InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = new Inventory();
        inventory.setAvailableStock(inventoryRequestDTO.getAvailableStock());
        inventory.setReservedStock(inventoryRequestDTO.getReservedStock());
        // inventory.setProductId(inventoryRequestDTO.getProductId());
        return inventory;
    }

    public static InventoryResponseDTO toResponseDTO(Inventory inventory) {
        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(inventory.getId());
        inventoryResponseDTO.setAvailableStock(inventory.getAvailableStock());
        inventoryResponseDTO.setReservedStock(inventory.getReservedStock());
        inventoryResponseDTO.setProductId(inventory.getProduct().getId());
        return inventoryResponseDTO;
    }

}