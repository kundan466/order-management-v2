package com.order_management.demo.dto;

import com.order_management.demo.entity.Orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsResponseDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double subTotal;
    
}
