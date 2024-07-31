package com.backend.banking_app.mapper;

import com.backend.banking_app.dto.TransactionDTO;
import com.backend.banking_app.model.Transaction;

public class TransactionMapper {

    public  static Transaction mapToTransaction(TransactionDTO transactionDTO){
        Transaction transaction = new Transaction(
                transactionDTO.getTransactionType(),
                transactionDTO.getAmount(),
                transactionDTO.getAccountId(),
                transactionDTO.getRecipientId(),
                transactionDTO.getTransactionDate(),
                transactionDTO.getDescription()
        );

        return transaction;
    }


}
