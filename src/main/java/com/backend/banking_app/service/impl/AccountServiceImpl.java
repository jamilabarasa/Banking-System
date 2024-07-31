package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.TransactionDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.model.Account;
import com.backend.banking_app.model.Notification;
import com.backend.banking_app.model.enumerations.TransactionType;
import com.backend.banking_app.repository.AccountRepository;
import com.backend.banking_app.service.AccountService;
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
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private TransactionService transactionService;

    private NotificationService notificationService;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionService transactionService, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }

    @Override
    public Account getAccountById(Long id) {
        log.debug("Request to find account with ID {}", id);
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with ID: " + id));
        return account;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        log.debug("Request to find account with AccountNumber {}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with AccountNumber: " + accountNumber));
        return account;
    }

    @Override
    public List<Account> getUserAccounts(Long id) {
        List<Account> accounts = accountRepository.findAllByuserId(id);
        return accounts;
    }

    @Override
    @Transactional
    public Account createAccount(Account account) {
        log.debug("Request to save account {}", account.getAccountNumber());
        //check if account already exists
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            log.warn("Account exits with accountNumber {} ", account.getAccountNumber());
            throw new BadRequestException("Account exits with accountNumber " + account.getAccountNumber());
        }

        //save account
        Account account1 = accountRepository.save(account);

        //save notification
        Notification notification = new Notification(
                account.getUserId(),
                "Account created successfully",
                LocalDateTime.now()
        );

        notificationService.createNotification(notification);

        log.info("Account created successfully ,Account Id",account1.getId());

        return account1;
    }

    @Override
    public Account updateAccount(Account account, Long id) {
        log.debug("About to update account");

        Account account1 = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account does not exist"));

        if (account.getAccountType() != null) {
            account1.setAccountType(account.getAccountType());
        }

        accountRepository.save(account1);


        return account1;
    }


    @Override
    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {

        log.debug("Request to deposit to account with ID: {}", accountId);

        //validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero");
        }

        //find account
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with ID " + accountId));

        //calculate new balance
        BigDecimal newBalance = account.getBalance().add(amount);

        //update new balance
        account.setBalance(newBalance);

        //save updated account
        accountRepository.save(account);

        // Create transaction description
        String description = String.format("Deposited amount %s to account ID %d, new Balance %s", amount, accountId, newBalance);

        //initialize transaction
        TransactionDTO transaction = new TransactionDTO(
                TransactionType.DEPOSIT,
                amount,
                accountId,
                accountId,
                LocalDateTime.now(),
                description

        );

        //save transaction
        transactionService.createTransaction(transaction);

        String message = String.format("Deposit of %s was successful",amount);

        //save notification
        Notification notification = new Notification(
                account.getUserId(),
                message,
                LocalDateTime.now()
        );

        notificationService.createNotification(notification);

        log.debug(description);

    }

    @Override
    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {

        log.debug("Request to withdraw to account with ID: {}", accountId);

        //validate amount
        if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than 100");
        }

        //find account
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with ID " + accountId));

        BigDecimal minAmount = account.getBalance().min(amount);

        //check if balance is less than amount to withdraw
        if (minAmount == account.getBalance()) {
            throw new BadRequestException("Insufficient Balance");
        }

        //set new balance
        BigDecimal newBalance = account.getBalance().subtract(amount);

        //update new balance
        account.setBalance(newBalance);

        //save updated account balance
        accountRepository.save(account);

        //prepare transaction description
        String description = String.format("Withdrew amount %s from account with ID %s,new balance %s", amount, accountId, newBalance);

        //initialize transaction
        TransactionDTO transaction = new TransactionDTO(
                TransactionType.WITHDRAWAL,
                amount,
                accountId,
                accountId,
                LocalDateTime.now(),
                description
        );

        //SAVE transaction
        transactionService.createTransaction(transaction);

        String message = String.format("Withdrawal of %s was successful",amount);

        //save notification
        Notification notification = new Notification(
                account.getUserId(),
                message,
                LocalDateTime.now()
        );

        notificationService.createNotification(notification);

        log.debug(description);
    }


}
