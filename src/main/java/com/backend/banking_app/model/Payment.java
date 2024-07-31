package com.backend.banking_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private LocalDateTime paymentDate;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "User ID is required")
    private Long userId;

    public Payment(BigDecimal amount, Long accountId, Long userId) {
        this.amount = amount;
        this.accountId = accountId;
        this.userId = userId;
    }
}
