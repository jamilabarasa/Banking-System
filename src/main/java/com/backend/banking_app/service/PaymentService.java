package com.backend.banking_app.service;

import com.backend.banking_app.dto.PaymentDTO;
import com.backend.banking_app.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment getPaymentById(Long id);

    Payment createPayment(PaymentDTO payment);

    List<Payment> getUserPayments(Long userId);


}
