package com.order_management.demo.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.order_management.demo.entity.RefreshToken;
import com.order_management.demo.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    long countByUserAndRevokedFalse(User user);

    List<RefreshToken> findByUserAndRevokedFalse(User user);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);

    
} 
