package com.order_management.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_management.demo.enums.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequestDTO {
    @NotNull
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
