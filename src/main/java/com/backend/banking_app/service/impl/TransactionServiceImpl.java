package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.TransactionDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.mapper.TransactionMapper;
import com.backend.banking_app.model.Account;
import com.backend.banking_app.model.Notification;
import com.backend.banking_app.model.Transaction;
import com.backend.banking_app.model.enumerations.TransactionType;
import com.backend.banking_app.repository.AccountRepository;
import com.backend.banking_app.repository.TransactionRepository;
import com.backend.banking_app.service.NotificationService;
import com.backend.banking_app.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    private NotificationService notificationService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }



    @Override
    public List<Transaction> getTransactionsByAccountId(Long id) {
        log.info("About to get transactions for user with ID {}",id);
        List<Transaction> transactions = transactionRepository.findAllByAccountId(id);
        return transactions;
    }

    @Override
    public Transaction createTransaction(TransactionDTO transactionDTO) {

        log.debug("About to create a transaction for account {}", transactionDTO.getAccountId());

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);

        //save transaction
        transactionRepository.save(transaction);

        return transactionRepository.save(transaction);
    }



    @Override
    @Transactional
    public void performTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        log.debug("About to tranfer {} from account with ID {} ,to account with ID {}",amount,fromAccountId,toAccountId);

        //check if from account exists
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(()->
                new ResourceNotFoundException("Account not found with ID " + fromAccountId));

        //check if to account exists
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(()->
                new ResourceNotFoundException("Account not found with ID " + toAccountId));

        //check if from account balance is more than amount
        BigDecimal minAmount = fromAccount.getBalance().min(amount);
        if(minAmount == fromAccount.getBalance()){
            log.warn("Insufficient Balance to transfer {} current balance is {}",amount,fromAccount.getBalance());
            throw new BadRequestException("Insufficient Balance to transfer " + amount + ". Your current balance is " + fromAccount.getBalance());
        }

        //get the new balance ,subtract from fromAcc and add to toAcc
        BigDecimal fromAccountNewBalance = fromAccount.getBalance().subtract(amount)  ;
        BigDecimal toAccountNewBalance = toAccount.getBalance().add(amount);

        //save new balance
        fromAccount.setBalance(fromAccountNewBalance);
        toAccount.setBalance(toAccountNewBalance);

        //update table
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String description = String.format("Transferred %s from account with id %s to account with id %s",amount,fromAccountId,toAccountId);

        //initialize transaction
        TransactionDTO transactionDTO = new TransactionDTO(
                TransactionType.TRANSFER,
                amount,
                fromAccountId,
                toAccountId,
                LocalDateTime.now(),
                description

        );

        //call create transaction method to save transaction
        createTransaction(transactionDTO);

        String message = String.format("Transfer of %s was successful",amount);

        //save notification
        Notification notification = new Notification(
                fromAccountId,
                message,
                LocalDateTime.now()
        );

        notificationService.createNotification(notification);

        log.debug(description);
    }


}
