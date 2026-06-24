package com.order_management.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order_management.demo.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders,Long>{

    List<Orders> findByUserId(Long id);

    Page<Orders> findByUserId(Long userId, Pageable pageable);
    
}
