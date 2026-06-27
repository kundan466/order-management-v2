package com.order_management.demo.controller;

import com.order_management.demo.repository.OrdersRepository;
import com.order_management.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order_management.demo.Utils.CurrentUserDetails;
import com.order_management.demo.dto.OrderRequestDTO;
import com.order_management.demo.dto.OrderResponseDTO;
import com.order_management.demo.dto.OrderStatusRequestDTO;
import com.order_management.demo.entity.User;
import com.order_management.demo.enums.UserRole;
import com.order_management.demo.exception.OrderNotFoundException;
import com.order_management.demo.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrdersRepository ordersRepository;

    private final UserRepository userRepository;

    @Autowired
    CurrentUserDetails currentUserDetails;
    

    private final OrderService orderService;
    public OrderController(OrderService orderService, UserRepository userRepository, OrdersRepository ordersRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
    }

    @PostMapping("/createorder")
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        //TODO: process POST request
        OrderResponseDTO order = orderService.createOrder(orderRequestDTO);
        return order;
    }

    @GetMapping("/getorders")  //working for roles
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/getorders/paginated")  //working as roles
    public Page<OrderResponseDTO> getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return orderService.getAllOrders(page, size);
    }


    @GetMapping("/getorder/{id}") //working for roles
    public OrderResponseDTO getOrder(@PathVariable Long id) {
        // TODO: Implement get order by ID
        // throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
        User user=currentUserDetails.getUserDetails();
        if(user.getRole().equals(UserRole.CUSTOMER)){
        return orderService.getAllOrders().stream()
                .filter(order -> order.getId().equals(id) && order.getUserId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id + "for the user id : " + user.getId()));
       }else{
         return orderService.getAllOrders().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id + "for the user"));
       }

}

    @GetMapping("/getordersbyuser/{userId}") //working as expected for roles(role based access contorl+ ownership validation that has done is service layer)
    public List<OrderResponseDTO> getOrdersByUserId(@PathVariable Long userId) {
        // TODO: Implement get orders by user ID
        // throw new UnsupportedOperationException("Unimplemented method 'getOrdersByUserId'");
        User currUser=currentUserDetails.getUserDetails();
        if(currUser.getRole() == UserRole.CUSTOMER && !userId.equals(currentUserDetails.getUserDetails().getId())){
            throw new AccessDeniedException("You cannot access another user's order!");
        }
        if(ordersRepository.findByUserId(userId).isEmpty()){
            throw new OrderNotFoundException("Orders are not present for this user: "+ userId);
        }
        return orderService.getAllOrders().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }

    @PatchMapping("/updateorderstatus/{id}")
    public OrderResponseDTO updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequestDTO orderStatusRequestDTO) {
        // TODO: Implement update order status
        // throw new UnsupportedOperationException("Unimplemented method 'updateOrderStatus'");
        return orderService.updateOrderStatus(id, orderStatusRequestDTO);
    }



}
