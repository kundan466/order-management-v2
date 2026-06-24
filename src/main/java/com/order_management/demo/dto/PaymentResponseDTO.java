package com.order_management.demo.dto;

import com.order_management.demo.enums.PaymentMode;
import com.order_management.demo.enums.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    private double amount; 
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
