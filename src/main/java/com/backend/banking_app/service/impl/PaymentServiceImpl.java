package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.PaymentDTO;
import com.backend.banking_app.dto.TransactionDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.mapper.PaymentMapper;
import com.backend.banking_app.model.Account;
import com.backend.banking_app.model.Notification;
import com.backend.banking_app.model.Payment;
import com.backend.banking_app.model.enumerations.TransactionType;
import com.backend.banking_app.repository.AccountRepository;
import com.backend.banking_app.repository.PaymentRepository;
import com.backend.banking_app.repository.UserRepository;
import com.backend.banking_app.service.NotificationService;
import com.backend.banking_app.service.PaymentService;
import com.backend.banking_app.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    private UserRepository userRepository;

    private AccountRepository accountRepository;

    private TransactionService transactionService;

    private NotificationService notificationService;


    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, AccountRepository accountRepository, TransactionService transactionService, NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }

    @Override
    public Payment getPaymentById(Long id) {
        log.debug("About to get payment with ID {}", id);
        //check if payment exists
        Payment payment = paymentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Payment not found with ID {}" + id));

        return payment;
    }

    @Override
    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO) {
        log.debug("About to create a payment");

        Payment payment = PaymentMapper.maptoPayment(paymentDTO);

        //check if user and account exits
        if (!userRepository.existsById(payment.getUserId())) {
            log.warn("User not found with ID {}", payment.getUserId());
            throw new ResourceNotFoundException("User not found with ID " + payment.getUserId());
        }

        //find account
        Account account = accountRepository.findById(payment.getAccountId()).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with ID " + payment.getAccountId()));

        //check if account balance is sufficient to make payment
        BigDecimal minAmount = account.getBalance().min(payment.getAmount());

        //check if balance is less than amount to withdraw
        if (minAmount == account.getBalance()) {
            throw new BadRequestException("Insufficient Balance");
        }


        //set new balance
        BigDecimal newBalance = account.getBalance().subtract(payment.getAmount());

        //update new balance
        account.setBalance(newBalance);

        //save updated account balance
        accountRepository.save(account);

        //set payment date to now
        payment.setPaymentDate(LocalDateTime.now());

        //save payment
        Payment payment1 = paymentRepository.save(payment);

        //prepare transaction description
        String description = String.format("Payment amount %s from account with ID %s,new balance %s", payment.getAmount(), payment.getAccountId(), newBalance);

        //initialize transaction
        TransactionDTO transaction = new TransactionDTO(
                TransactionType.PAYMENT,
                payment.getAmount(),
                payment.getAccountId(),
                null,
                LocalDateTime.now(),
                description
        );

        //SAVE transaction
        transactionService.createTransaction(transaction);

        String message = String.format("Payement of %s was successful", payment.getAmount());

        //save notification
        Notification notification = new Notification(
                payment.getUserId(),
                message,
                LocalDateTime.now()
        );

        notificationService.createNotification(notification);

        log.debug(description);

        return payment1;
    }


    @Override
    public List<Payment> getUserPayments(Long userId) {
        log.debug("About to find payments for user with ID {}", userId);

        //check if user exists
        if (!userRepository.existsById(userId)) {
            log.debug("User not found with ID {}", userId);
            throw new ResourceNotFoundException("User not found with ID " + userId);
        }

        List<Payment> payments = paymentRepository.findAllByUserId(userId);


        return payments;
    }
}
