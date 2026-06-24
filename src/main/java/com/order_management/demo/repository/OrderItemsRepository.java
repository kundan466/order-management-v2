package com.order_management.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order_management.demo.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Long>{
    
}
