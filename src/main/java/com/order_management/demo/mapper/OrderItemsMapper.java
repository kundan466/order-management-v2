package com.order_management.demo.mapper;

import java.util.List;

import com.order_management.demo.dto.OrderItemsResponseDTO;
import com.order_management.demo.entity.OrderItems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsMapper {

    public static List<OrderItemsResponseDTO> toResponseDTO(List<OrderItems> orderItems) {
        return orderItems.stream().map(OrderItemsMapper::toResponseDTO).toList();
    }

    public static OrderItemsResponseDTO toResponseDTO(OrderItems orderItem) {
        OrderItemsResponseDTO responseDTO = new OrderItemsResponseDTO();
        responseDTO.setProductId(orderItem.getProduct().getId());
        responseDTO.setProductName(orderItem.getProduct().getName());
        responseDTO.setQuantity(orderItem.getQuantity());
        responseDTO.setPrice(orderItem.getPrice());
        responseDTO.setSubTotal(orderItem.getSubTotal());
        return responseDTO;
    }
}
