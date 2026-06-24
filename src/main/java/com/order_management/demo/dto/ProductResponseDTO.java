package com.order_management.demo.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProductResponseDTO implements Serializable{
    
    private Long id;
    private String name;
    private String category;
    private String description;
    private String brand;
    private String dealerName;
    private double originalPrice;
    private double sellingPrice;
    private double discount;
}
