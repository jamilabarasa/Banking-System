package com.backend.banking_app.service;

import com.backend.banking_app.dto.LoanDTO;
import com.backend.banking_app.model.Loan;
import com.backend.banking_app.model.enumerations.LoanStatus;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {

    Loan getLoanById(Long id);

    Loan createLoan(LoanDTO loan);

    Loan payLoan(Long id, BigDecimal amount);

    List<Loan> userLoans(Long userId);

    Loan updateLoanStatus(Long id, LoanStatus status);

    void  loanPaymentReminder(Long userId);
}
