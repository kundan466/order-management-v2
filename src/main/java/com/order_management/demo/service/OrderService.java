package com.order_management.demo.service;
import com.order_management.demo.DemoApplication;
import com.order_management.demo.Utils.CurrentUserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.order_management.demo.dto.OrderItemsRequestDTO;
import com.order_management.demo.dto.OrderRequestDTO;
import com.order_management.demo.dto.OrderResponseDTO;
import com.order_management.demo.dto.OrderStatusRequestDTO;
import com.order_management.demo.entity.Inventory;
import com.order_management.demo.entity.OrderItems;
import com.order_management.demo.entity.Orders;
import com.order_management.demo.entity.Product;
import com.order_management.demo.entity.User;
import com.order_management.demo.enums.OrderStatus;
import com.order_management.demo.enums.UserRole;
import com.order_management.demo.mapper.OrdersMapper;
import com.order_management.demo.repository.InventoryRepository;
import com.order_management.demo.repository.OrderItemsRepository;
import com.order_management.demo.repository.OrdersRepository;
import com.order_management.demo.repository.ProductRepository;
import com.order_management.demo.repository.UserRepository;
import com.order_management.demo.exception.OrderNotFoundException;
import com.order_management.demo.exception.ProductNotFoundException;
import com.order_management.demo.exception.UserNotFoundException;
import com.order_management.demo.exception.InvalidOrderStatusException;
import com.order_management.demo.exception.InventoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import jakarta.transaction.Transactional;


@Service

public class OrderService {
    private final DemoApplication demoApplication;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    OrderItemsRepository orderItemsRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    CurrentUserDetails currentUserDetails;

    OrderService(DemoApplication demoApplication) {
        this.demoApplication = demoApplication;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'createOrder'");

        // Validate user existence
        User existingUser = userRepository.findById(orderRequestDTO.getUserId()).orElseThrow(() -> {
            return new UserNotFoundException("User not found");
        });

        

        // Validate product existence
        for(OrderItemsRequestDTO item : orderRequestDTO.getOrderItems()) {
            Product existingProduct = productRepository.findById(item.getProductId()).orElseThrow(() -> {
                return new ProductNotFoundException("Product not found");
            });
            // totalPrice += existingProduct.getSellingPrice() * item.getQuantity();
            

            Inventory inventory = inventoryRepository.findByProductId(existingProduct.getId()).orElseThrow(() -> {
                return new InventoryNotFoundException("Inventory not found for the product");
            });
            if (inventory.getAvailableStock() < item.getQuantity()) {
                 throw new ProductNotFoundException("Product is out of stock");
            }
        }


        //create orderItems and calculate total price
        List<OrderItems> orderItems = new ArrayList<OrderItems>();
            for(OrderItemsRequestDTO item : orderRequestDTO.getOrderItems()) {
                Product existingProduct = productRepository.findById(item.getProductId()).orElseThrow(() -> {
                    return new ProductNotFoundException("Product not found");
                });
                OrderItems orderItem = new OrderItems();
                orderItem.setProduct(existingProduct);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(existingProduct.getSellingPrice());
                orderItem.setSubTotal(existingProduct.getSellingPrice() * item.getQuantity());
                orderItems.add(orderItem);
                // log.debug("Order Item: Product ID - {}, Quantity - {}, Price - {}, SubTotal - {}", existingProduct.getId(), item.getQuantity(), existingProduct.getSellingPrice(), orderItem.getSubTotal());
            }
            
            double totalPrice = orderItems.stream().mapToDouble(OrderItems::getSubTotal).sum();
            
        

        Orders order = new Orders();
        // Create and save the order
        order.setUser(existingUser);
        order.setOrderItems(orderItems);
        order.setDeliveryAddress(orderRequestDTO.getDeliveryAddress());
        order.setTotalAmount(totalPrice);    
        order.setOrderStatus(OrderStatus.PLACED); 

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setCreatedBy(currentUserDetails.getUserDetails().getId());
        order.setUpdatedBy(currentUserDetails.getUserDetails().getId());

        ordersRepository.save(order);

        orderItems.forEach(item -> {
            item.setOrder(order);
            orderItemsRepository.save(item);});
        
        // int x = 10 / 0;


         // Update inventory
        for(OrderItemsRequestDTO item : orderRequestDTO.getOrderItems()) {
             
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId()).orElseThrow(() -> {
                return new InventoryNotFoundException("Inventory not found for the product");
            });
            
            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            inventory.setReservedStock(inventory.getReservedStock() + item.getQuantity());
            
            inventory.setUpdatedAt(LocalDateTime.now());
            inventory.setUpdatedBy(currentUserDetails.getUserDetails().getId());
            inventoryRepository.save(inventory);
        }

        // ... set other order properties

        // Save the order (assuming you have an OrderRepository)
        // orderRepository.save(order);

        // Return the response DTO
        return ordersMapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllOrders'");

        // to get order of paricular user
        User user= currentUserDetails.getUserDetails();
        if(user.getRole().equals(UserRole.CUSTOMER)){
        List<Orders> orders = ordersRepository.findByUserId(user.getId());
        if(orders.isEmpty()){
            throw new OrderNotFoundException("this user has no orders");
            }
            return orders.stream().map(ordersMapper::toResponseDTO).toList();
        }
        else{
            List<Orders> orders = ordersRepository.findAll();
            if(orders.isEmpty()){
            throw new OrderNotFoundException("this user has no orders");
            }
            return orders.stream().map(ordersMapper::toResponseDTO).toList();
        }
    }
    
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusRequestDTO orderStatusRequestDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateOrderStatus'");
        User currentUser = currentUserDetails.getUserDetails();
        // Orders existingOrder=null;
       
        Orders existingOrder = ordersRepository.findById(id).orElseThrow(() -> {
        return new OrderNotFoundException("Order not found");
        });
    

    if(currentUser.getRole().equals(UserRole.CUSTOMER) && !existingOrder.getUser().getId().equals(currentUser.getId())){
        throw new AccessDeniedException(
                "You cannot update another user's order");
    }
    
        
        
        OrderStatus currentStatus = existingOrder.getOrderStatus();
        if(currentUser.getRole().equals(UserRole.CUSTOMER) && !orderStatusRequestDTO.getOrderStatus().equals(OrderStatus.CANCELLED)){
                throw new  AccessDeniedException("User cannot modify the status otherthan cancel");
    
        }
        OrderStatus newStatus = orderStatusRequestDTO.getOrderStatus();
    
        //validations of statuses is only for customer not for admin
        if(currentUser.getRole().equals(UserRole.CUSTOMER)){
            validate(currentStatus, newStatus);
        }
    
        //Update the status to customer only if it cancelled or update any status to admin
        if((currentUser.getRole().equals(UserRole.CUSTOMER) && newStatus.equals(OrderStatus.CANCELLED))|| currentUser.getRole().equals(UserRole.ADMIN)){
        existingOrder.setOrderStatus(newStatus);
        
        existingOrder.setUpdatedAt(LocalDateTime.now());
        existingOrder.setUpdatedBy(currentUserDetails.getUserDetails().getId());
        ordersRepository.save(existingOrder);
        }

        if(OrderStatus.CANCELLED==newStatus) {
            // Update inventory
            for(OrderItems item : existingOrder.getOrderItems()) {
                Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId()).orElseThrow(() -> {
                    return new InventoryNotFoundException("Inventory not found for the product");
                });
                inventory.setAvailableStock(inventory.getAvailableStock() + item.getQuantity());
                inventory.setReservedStock(inventory.getReservedStock() - item.getQuantity());
                
                inventory.setUpdatedAt(LocalDateTime.now());
                inventory.setUpdatedBy(currentUserDetails.getUserDetails().getId());
                inventoryRepository.save(inventory);
            }
        }
        return ordersMapper.toResponseDTO(existingOrder);
    }

    private void validate(OrderStatus currentStatus, OrderStatus newStatus) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'validate'");
    // currentStatus = currentStatus.trim().toUpperCase();
    // newStatus = newStatus.trim().toUpperCase();
    //validations of status transitions
    if(currentStatus.equals(OrderStatus.PLACED) && !newStatus.equals(OrderStatus.PROCESSING)
    && !newStatus.equals(OrderStatus.CANCELLED)) {
    throw new InvalidOrderStatusException( "Invalid status transition");
    }

    if(currentStatus.equals(OrderStatus.PROCESSING) && !newStatus.equals(OrderStatus.SHIPPED)
    && !newStatus.equals(OrderStatus.CANCELLED)) {
    throw new InvalidOrderStatusException("Invalid status transition");
    }

    if(currentStatus.equals(OrderStatus.SHIPPED) && !newStatus.equals(OrderStatus.DELIVERED)) {
    throw new InvalidOrderStatusException("Invalid status transition");
    }

    if(currentStatus.equals(OrderStatus.DELIVERED)) {
        throw new InvalidOrderStatusException("Delivered order cannot be modified");
    }

    if (newStatus == null) {
            throw new InvalidOrderStatusException("Order status is required");
    }
  
   }

    public Page<OrderResponseDTO> getAllOrders(int page, int size) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllOrders'");
        User user= currentUserDetails.getUserDetails();
        if(user.getRole().equals(UserRole.CUSTOMER)){
        // List<Orders> orders = ordersRepository.findByUserId(user.getId());
       Pageable pageable =PageRequest.of(page, size);
       Page<Orders> ordersPage =ordersRepository.findByUserId(user.getId(), pageable);
        return ordersPage.map(ordersMapper::toResponseDTO);
        }
        else{
            Page<Orders> ordersPage = ordersRepository.findAll(PageRequest.of(page, size));
            return ordersPage.map(ordersMapper::toResponseDTO);
        }
        
    }
    
}
