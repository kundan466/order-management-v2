package com.order_management.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequestDTO {
    @Email
    private String email;
    @NotBlank
    private String password;
}
