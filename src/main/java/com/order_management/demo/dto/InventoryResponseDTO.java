package com.order_management.demo.dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class InventoryResponseDTO {
    private Long id;
    private Long productId;
    private int availableStock;
    private int reservedStock;
    
}
