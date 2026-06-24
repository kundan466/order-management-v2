package com.order_management.demo.mapper;

import com.order_management.demo.dto.PaymentResponseDTO;
import com.order_management.demo.entity.Payments;

public class PaymentMapper {

    public static PaymentResponseDTO toResponseDTO(Payments payment) {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        paymentResponseDTO.setId(payment.getId());
        paymentResponseDTO.setPaymentMode(payment.getPaymentMode());
        paymentResponseDTO.setAmount(payment.getAmount());
        paymentResponseDTO.setPaymentStatus(payment.getPaymentStatus());
        return paymentResponseDTO;
    }
    
}
