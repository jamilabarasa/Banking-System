package com.backend.banking_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentDTO {


    private BigDecimal amount;

    private Long accountId;

    private Long userId;
}
