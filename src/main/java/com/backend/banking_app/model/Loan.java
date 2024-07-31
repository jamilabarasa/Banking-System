package com.backend.banking_app.model;

import com.backend.banking_app.model.enumerations.LoanPaymentStatus;
import com.backend.banking_app.model.enumerations.LoanStatus;
import com.backend.banking_app.model.enumerations.LoanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Loan Type is required")
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @NotNull(message = "Loan Amount is required")
    private BigDecimal amount;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Loan interestRate is required")
    private float interestRate;

    @NotNull(message = "Loan startDate is required")
    private LocalDate startDate;

    @NotNull(message = "Loan endDate is required")
    private LocalDate endDate;

    private BigDecimal cumulativeAmount =  BigDecimal.ZERO; // Default value of 0;;

    private BigDecimal paidAmount = BigDecimal.ZERO; // Default value of 0;;

    private BigDecimal remainingAmount = BigDecimal.ZERO; // Default value of 0;;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private LoanPaymentStatus loanPaymentStatus;

    public Loan(LoanType loanType, BigDecimal amount, Long userId, float interestRate, LocalDate startDate, LocalDate endDate) {
        this.loanType = loanType;
        this.amount = amount;
        this.userId = userId;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
