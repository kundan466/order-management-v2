package com.order_management.demo.dto;

import java.util.List;

import com.order_management.demo.entity.OrderItems;
import com.order_management.demo.entity.Payments;
import com.order_management.demo.enums.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {
    
    private Long Id;

    private Long userId;

    private String deliveryAddress;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private List<OrderItemsResponseDTO> items;
}
