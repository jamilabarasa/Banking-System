package com.backend.banking_app.controller;

import com.backend.banking_app.controller.vm.SuccessResponse;
import com.backend.banking_app.model.Account;
import com.backend.banking_app.model.Transaction;
import com.backend.banking_app.service.AccountService;
import com.backend.banking_app.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private AccountService accountService;

    private TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    //create account
    @PostMapping
    public ResponseEntity<SuccessResponse> createAccount(@Valid @RequestBody Account account) {
        log.debug("REST Request to create Account with account number {}", account.getAccountNumber());
        accountService.createAccount(account);
        SuccessResponse successResponse = new SuccessResponse(
                "Account Created Successfully",
                201
        );
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);

    }

    //get account by id
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        log.debug("REST Request to get Account with ID {}", accountId);
        return new ResponseEntity<>(accountService.getAccountById(accountId),HttpStatus.OK);
    }

    //get account by account number
    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        log.debug("REST Request to get Account with Account Number {}", accountNumber);
        return new ResponseEntity<>(accountService.getAccountByNumber(accountNumber),HttpStatus.OK);
    }

    //get all user accounts
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable Long id){
        log.debug("REST Request to get user Accounts with Account Number");
        return new ResponseEntity<>(accountService.getUserAccounts(id),HttpStatus.OK);
    }

    //update account
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateAccount(@Valid @RequestBody Account account,Long id) {
        log.debug("REST Request to update Account with account number {}", account.getAccountNumber());
        accountService.updateAccount(account,id);
        SuccessResponse successResponse = new SuccessResponse(
                "Account Updated Successfully",
                200
        );
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    //deposit
    @PutMapping("transactions/deposit")
    public ResponseEntity<SuccessResponse> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount){
        log.debug("REST Request to deposit {}, to Account ID {}", amount,accountId);
        accountService.deposit(accountId,amount);
        String message = String.format("Deposit of %s was successful",amount);
        SuccessResponse successResponse = new SuccessResponse(
                message,
                200
        );
        return new ResponseEntity<>(successResponse, HttpStatus.OK);

    }

    //withdraw
    @PutMapping("transactions/withdraw")
    public ResponseEntity<SuccessResponse> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount){
        log.debug("REST Request to withdraw {}, from Account ID {}", amount,accountId);
        accountService.withdraw(accountId,amount);
        String message = String.format("Withdrawal of %s was successful",amount);
        SuccessResponse successResponse = new SuccessResponse(
                message,
                200
        );
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }


    //get all transactions for an account
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> accountTransaction(@PathVariable Long accountId){
        log.debug("REST Request to get transaction for account with ID {}",accountId);
        return new ResponseEntity<>(transactionService.getTransactionsByAccountId(accountId),HttpStatus.OK);
    }


    //transfer fund
    @PutMapping("/transactions/transfer")
    public ResponseEntity<SuccessResponse> transfer(@RequestParam Long fromAccountId,@RequestParam Long toAccountId,@RequestParam BigDecimal amount){
        log.debug("REST Request to transfer {}, from Account ID {} to Account with ID {}", amount,fromAccountId,toAccountId);
        transactionService.performTransfer(fromAccountId,toAccountId,amount);
        String message = String.format("Transfer of %s was successful",amount);
        SuccessResponse successResponse = new SuccessResponse(
                message,
                200
        );
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
