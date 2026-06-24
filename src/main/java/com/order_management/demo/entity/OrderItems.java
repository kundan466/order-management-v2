package com.order_management.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderItems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    // OrderItem
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // private String productName;
    private int quantity;
    private double price;
    private double subTotal;   
}
