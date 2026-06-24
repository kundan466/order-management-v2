package com.order_management.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String accesstoken;
    private String refreshToken;
    private String role;
}
