package com.order_management.demo.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProductRequestDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    private String description;
    private String brand;
    private String dealerName;
    @Positive
    private double originalPrice;
    @Positive
    private double sellingPrice;
    @Positive
    private double discount;
    
}
