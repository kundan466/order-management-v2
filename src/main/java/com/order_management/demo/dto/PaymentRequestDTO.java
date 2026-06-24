package com.order_management.demo.dto;


import com.order_management.demo.entity.Orders;
import com.order_management.demo.enums.PaymentMode;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    @NotBlank
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
}
