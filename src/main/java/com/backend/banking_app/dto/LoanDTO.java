package com.backend.banking_app.dto;


import com.backend.banking_app.model.enumerations.LoanType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanDTO {

    private LoanType loanType;

    private BigDecimal amount;

    private Long userId;

    private float interestRate;

    private LocalDate startDate;

    private LocalDate endDate;


}
