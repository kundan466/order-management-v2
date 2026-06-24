package com.order_management.demo.service;



import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.order_management.demo.Utils.CurrentUserDetails;
import com.order_management.demo.dto.PaymentRequestDTO;
import com.order_management.demo.dto.PaymentResponseDTO;
import com.order_management.demo.entity.Orders;
import com.order_management.demo.entity.Payments;
import com.order_management.demo.entity.User;
import com.order_management.demo.enums.OrderStatus;
import com.order_management.demo.enums.PaymentStatus;
import com.order_management.demo.enums.UserRole;
import com.order_management.demo.exception.OrderNotFoundException;
import com.order_management.demo.exception.PaymentAlreadyExistsException;
import com.order_management.demo.exception.PaymentInvalidException;
import com.order_management.demo.mapper.PaymentMapper;
import com.order_management.demo.repository.OrdersRepository;
import com.order_management.demo.repository.PaymentRepository;
import com.order_management.demo.repository.UserRepository;


@Service
public class PaymentService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrdersRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CurrentUserDetails currentUser;
    @Autowired
    CurrentUserDetails currentUserDetails;
    public void doPayment(Long id, PaymentRequestDTO paymentRequest) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'doPayment'");
        
        Orders existingOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        User curr_User= currentUser.getUserDetails();
        if(curr_User.getRole().equals(UserRole.CUSTOMER) && !curr_User.getId().equals(existingOrder.getUser().getId())){
             throw new PaymentInvalidException("Payment is belongs to the users who are logged in");
        }
        
        if(existingOrder.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new PaymentInvalidException("Payment cannot be processed for cancelled orders");
        }

        if(paymentRepository.findByOrderId(id).isPresent()) {
            throw new PaymentAlreadyExistsException("Payment has already been processed for this order");
        }

        Payments payment = new Payments();
        payment.setOrder(existingOrder);
        payment.setPaymentMode(paymentRequest.getPaymentMode());
        payment.setAmount(existingOrder.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setCreatedBy(currentUserDetails.getUserDetails().getId());
        payment.setUpdatedBy(currentUserDetails.getUserDetails().getId());

        paymentRepository.save(payment);

    }

    public Page<PaymentResponseDTO> getAllPayments(int int1, int int2) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllPayments'");
        Page<Payments> paymentsPage = paymentRepository.findAll(PageRequest.of(int1, int2));
        return paymentsPage.map(PaymentMapper::toResponseDTO);
    }
    
}
