package com.order_management.demo.dto;
import com.order_management.demo.enums.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequestDTO {
    private String name;
    private String email;
    private String address;
    private Long phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
