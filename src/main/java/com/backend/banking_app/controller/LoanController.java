package com.backend.banking_app.controller;

import com.backend.banking_app.controller.vm.SuccessResponse;
import com.backend.banking_app.dto.LoanDTO;
import com.backend.banking_app.model.Loan;
import com.backend.banking_app.model.enumerations.LoanStatus;
import com.backend.banking_app.service.LoanService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/loans")
public class LoanController {


    private LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    //create a loan
    @PostMapping
    public ResponseEntity<SuccessResponse> createLoan(@Valid @RequestBody LoanDTO loan ){
        log.debug("REST Request to create loan for user with ID {}",loan.getUserId());
        loanService.createLoan(loan);
        SuccessResponse successResponse = new SuccessResponse(
                "Loan created successfully",
                201
        );
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    //get a loan by id
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id){
        log.debug("REST Request to get Loan by ID {}",id);
        Loan loan = loanService.getLoanById(id);
        return new ResponseEntity<>(loan,HttpStatus.OK);
    }

    //get user loans
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> userLoans(@PathVariable Long userId){
        log.debug("REST Request to get loans for user with ID {}",userId);
        return new ResponseEntity<>(loanService.userLoans(userId),HttpStatus.OK);
    }

    //update loan status
    @PutMapping("/status")
    public ResponseEntity<Loan> updateLoanStatus(@RequestParam Long id,@RequestParam LoanStatus status){
        log.debug("About to update loan status of loan with ID {}",id);
        Loan loan = loanService.updateLoanStatus(id,status);
        return new ResponseEntity<>(loan,HttpStatus.OK);
    }

    //loan reminder
    @PostMapping("/reminder/{userId}")
    public ResponseEntity<SuccessResponse> loanReminder(@PathVariable Long userId){
        log.debug("REST Request to send loan payment reminder to user with ID",userId);
        loanService.loanPaymentReminder(userId);
        SuccessResponse successResponse = new SuccessResponse(
                "Loan Reminder sent successfully",
                201
        );
        return new ResponseEntity<>(successResponse,HttpStatus.CREATED);

    }

    //loan payment
    @PutMapping("/payment")
    public ResponseEntity<Loan> payLoan(@RequestParam Long loanId, @RequestParam BigDecimal amount){
        log.debug("About to pay loan with ID {}",loanId);
        Loan loan = loanService.payLoan(loanId,amount);
        return new ResponseEntity<>(loan,HttpStatus.OK);

    }

}
