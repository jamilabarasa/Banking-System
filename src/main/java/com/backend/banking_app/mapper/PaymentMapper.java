package com.backend.banking_app.mapper;

import com.backend.banking_app.dto.PaymentDTO;
import com.backend.banking_app.model.Payment;

public class PaymentMapper {

    public static Payment maptoPayment(PaymentDTO paymentDTO){
        Payment payment = new Payment(
                paymentDTO.getAmount(),
                paymentDTO.getAccountId(),
                paymentDTO.getUserId()
        );
        return payment;
    }
}
