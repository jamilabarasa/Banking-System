package com.backend.banking_app.dto;

import com.backend.banking_app.model.enumerations.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private TransactionType transactionType;
    private BigDecimal amount;
    private Long accountId;
    private Long recipientId;
    private LocalDateTime transactionDate;
    private String description;


}
