package com.order_management.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.order_management.demo.entity.Payments;

public interface PaymentRepository extends JpaRepository<Payments,Long>{

    Optional<Payments> findByOrderId(Long id);
    
}
