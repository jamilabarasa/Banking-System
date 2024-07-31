package com.backend.banking_app.controller;

import com.backend.banking_app.dto.PaymentDTO;
import com.backend.banking_app.model.Payment;
import com.backend.banking_app.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id){
        log.debug("REST Request to get Payment by ID {}",id);
        Payment payment = paymentService.getPaymentById(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable Long userId){
        log.debug("REST Request to get all payments for user with ID {}",userId);
        List<Payment> payments = paymentService.getUserPayments(userId);
        return new ResponseEntity<>(payments,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDTO payment){
        log.debug("REST Request to create payment");
        Payment payment1 = paymentService.createPayment(payment);
        return new ResponseEntity<>(payment1,HttpStatus.CREATED);
    }



}
