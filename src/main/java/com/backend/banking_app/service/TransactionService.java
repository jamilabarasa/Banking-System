package com.backend.banking_app.service;

import com.backend.banking_app.dto.TransactionDTO;
import com.backend.banking_app.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactionsByAccountId(Long id);

    Transaction createTransaction(TransactionDTO transactionDTO);

    void performTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
