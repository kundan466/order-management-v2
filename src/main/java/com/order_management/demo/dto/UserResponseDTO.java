package com.order_management.demo.dto;
import org.springframework.stereotype.Component;

import com.order_management.demo.enums.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class UserResponseDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private Long phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
   
}
