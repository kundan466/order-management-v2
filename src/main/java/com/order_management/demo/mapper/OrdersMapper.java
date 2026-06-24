package com.order_management.demo.mapper;

import org.springframework.stereotype.Component;

import com.order_management.demo.dto.OrderRequestDTO;
import com.order_management.demo.dto.OrderResponseDTO;
import com.order_management.demo.entity.Orders;

@Component
public class OrdersMapper {

    public OrderResponseDTO toResponseDTO(Orders order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setUserId(order.getUser().getId());
        orderResponseDTO.setItems(order.getOrderItems().stream().map(OrderItemsMapper::toResponseDTO).toList());
        orderResponseDTO.setDeliveryAddress(order.getDeliveryAddress());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setOrderStatus(order.getOrderStatus());
        // Map other fields as needed
        return orderResponseDTO;
    }
    
}
