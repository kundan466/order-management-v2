package com.order_management.demo.dto;

import java.util.List;

import com.order_management.demo.entity.OrderItems;
import com.order_management.demo.entity.Payments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    
    @NotNull
    private List<OrderItemsRequestDTO> orderItems;
    // private Payments payment;
    @NotNull
    private Long userId;    
    @NotBlank
    private String deliveryAddress;
    // private double totalAmount;
    // private String orderStatus;
}
