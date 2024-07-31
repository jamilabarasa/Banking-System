package com.backend.banking_app.model;


import com.backend.banking_app.model.enumerations.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "recipient_account_id")
    private Long RecipientAccountId;


    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;


    private String description;

    public Transaction(TransactionType transactionType, BigDecimal amount, Long accountId, Long recipientAccountId, LocalDateTime transactionDate, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountId = accountId;
        RecipientAccountId = recipientAccountId;
        this.transactionDate = transactionDate;
        this.description = description;
    }
}
