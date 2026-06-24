package com.order_management.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsRequestDTO {

    @NotNull
    private Long productId;
    @Positive
    private int quantity;
    // private double price;
    // private double subTotal;
    
}
