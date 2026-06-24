package com.order_management.demo.exception;

public class ObjectOptimisticLockingFailureException extends RuntimeException{
    public ObjectOptimisticLockingFailureException(String message) {
        super(message);
    }
}
