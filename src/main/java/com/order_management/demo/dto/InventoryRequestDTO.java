package com.order_management.demo.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class InventoryRequestDTO {
    
    @NotNull
    private Long productId;
    @Positive
    private int availableStock;
    @Positive
    private int reservedStock;
}
