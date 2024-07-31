package com.backend.banking_app.service;

import com.backend.banking_app.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account getAccountById(Long id);
    Account getAccountByNumber(String accountNumber);
    List<Account> getUserAccounts(Long id);
    Account createAccount(Account account);
    Account updateAccount(Account account,Long id);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
}
