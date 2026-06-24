package com.order_management.demo.mapper;

import org.springframework.stereotype.Component;
import com.order_management.demo.dto.UserRequestDTO;
import com.order_management.demo.dto.UserResponseDTO;
import com.order_management.demo.entity.User;


@Component
public class UserMapper {
    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setAddress(user.getAddress());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhone_number());
        dto.setStatus(user.getStatus());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPhone_number(dto.getPhoneNumber());
        user.setStatus(dto.getStatus());
        user.setPassword(dto.getPassword());
        return user;
    }
}
