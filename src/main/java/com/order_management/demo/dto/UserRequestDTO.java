package com.order_management.demo.dto;

import org.springframework.stereotype.Component;

import com.order_management.demo.enums.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class UserRequestDTO {
    @NotBlank   
    private String name;
    @Email
    private String email;
    @NotBlank
    private String address;
    @NotNull
    private Long phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @NotBlank
    private String password;
}
