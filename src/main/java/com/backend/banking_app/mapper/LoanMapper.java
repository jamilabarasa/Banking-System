package com.backend.banking_app.mapper;

import com.backend.banking_app.dto.LoanDTO;
import com.backend.banking_app.model.Loan;

public class LoanMapper {

    public static Loan mapToLoan(LoanDTO loanDTO){

        Loan loan = new Loan(
                loanDTO.getLoanType(),
                loanDTO.getAmount(),
                loanDTO.getUserId(),
                loanDTO.getInterestRate(),
                loanDTO.getStartDate(),
                loanDTO.getEndDate()
        );

        return loan;

    }
}
