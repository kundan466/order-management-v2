package com.order_management.demo.exception;

public class PaymentInvalidException extends RuntimeException {
    public PaymentInvalidException(String message) {
        super(message);
    }
}