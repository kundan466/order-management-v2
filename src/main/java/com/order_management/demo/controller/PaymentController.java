package com.order_management.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order_management.demo.dto.PaymentRequestDTO;
import com.order_management.demo.dto.PaymentResponseDTO;
import com.order_management.demo.exception.PaymentInvalidException;
import com.order_management.demo.mapper.PaymentMapper;
import com.order_management.demo.repository.PaymentRepository;
import com.order_management.demo.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    PaymentService paymentService;

     public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/dopayment/{id}")
    public String doPayment(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        //TODO: process POST request
        paymentService.doPayment(id, paymentRequest);
        
        return "Payment processed successfully for order ID: " + id + "using payment mode: " + paymentRequest.getPaymentMode() ;
    }

    @GetMapping("/getallpayments") //working for roles
    public List<PaymentResponseDTO> getMethodName() {
        return paymentRepository.findAll().stream().map(PaymentMapper::toResponseDTO).toList();
    }

    @GetMapping("/getallpayments/paginated")  //working for roles
    public Page<PaymentResponseDTO> getMethodName(@RequestParam String page, @RequestParam String size) {
        return paymentService.getAllPayments(Integer.parseInt(page), Integer.parseInt(size));
    }
    
    
    @GetMapping("/getpayment/{id}")  //working for roles
    public PaymentResponseDTO getPaymentByID(@PathVariable Long id) {
        return paymentRepository.findById(id).map(PaymentMapper::toResponseDTO).orElseThrow(() -> new PaymentInvalidException("Payment not found with ID: " + id));
    }

    @GetMapping("/getpaymentbyorderid/{id}")  //working for roles
    public List<PaymentResponseDTO> getPaymentByOrderID(@PathVariable Long id) {
        return paymentRepository.findByOrderId(id).stream().map(PaymentMapper::toResponseDTO).toList();
    }


    
    
    
}
