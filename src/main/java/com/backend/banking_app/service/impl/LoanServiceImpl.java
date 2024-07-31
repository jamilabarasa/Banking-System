package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.LoanDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.mapper.LoanMapper;
import com.backend.banking_app.model.Loan;
import com.backend.banking_app.model.Notification;
import com.backend.banking_app.model.enumerations.LoanPaymentStatus;
import com.backend.banking_app.model.enumerations.LoanStatus;
import com.backend.banking_app.repository.LoanRepository;
import com.backend.banking_app.repository.UserRepository;
import com.backend.banking_app.service.LoanService;
import com.backend.banking_app.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, NotificationService notificationService, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Override
    public Loan getLoanById(Long id) {
        log.debug("Fetching loan by ID: {}", id);
        return loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID " + id));
    }

    @Override
    @Transactional
    public Loan createLoan(LoanDTO loanDTO) {
        log.debug("Creating a loan for user ID: {}", loanDTO.getUserId());

        //convert dto to loan
        Loan loan = LoanMapper.mapToLoan(loanDTO);

        BigDecimal interestAmount = loan.getAmount().multiply(BigDecimal.valueOf(loan.getInterestRate() / 100));
        BigDecimal cumulativeAmount = loan.getAmount().add(interestAmount);

        log.info("Total amount to be paid back: {}", cumulativeAmount);

        loan.setCumulativeAmount(cumulativeAmount);

        //set loan status to processing
        loan.setLoanStatus(LoanStatus.PROCESSING);

        Loan savedLoan = loanRepository.save(loan);

        Notification notification = new Notification(
                savedLoan.getUserId(),
                "Loan created successfully",
                LocalDateTime.now()
        );
        notificationService.createNotification(notification);

        log.info("Loan created successfully, Loan ID: {}", savedLoan.getId());
        return savedLoan;
    }

    @Override
    @Transactional
    public Loan payLoan(Long id, BigDecimal amount) {
        log.debug("Paying loan ID: {}", id);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount to be paid should be greater than 0");
        }

        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID " + id));

        loan.setPaidAmount(loan.getPaidAmount().add(amount));
        loan.setRemainingAmount(loan.getCumulativeAmount().subtract(loan.getPaidAmount()));

        if (loan.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
            loan.setLoanPaymentStatus(loan.getPaidAmount().compareTo(BigDecimal.ZERO) > 0 ? LoanPaymentStatus.PARTIAL : LoanPaymentStatus.UNPAID);
        } else {
            loan.setLoanPaymentStatus(LoanPaymentStatus.PAID);
        }

        Loan updatedLoan = loanRepository.save(loan);
        String message = String.format("Payment of %s to Loan ID %d was successful. Remaining Loan balance is %s", amount, updatedLoan.getId(), updatedLoan.getRemainingAmount());

        Notification notification = new Notification(
                updatedLoan.getUserId(),
                message,
                LocalDateTime.now()
        );
        notificationService.createNotification(notification);

        log.info(message);
        return updatedLoan;
    }

    @Override
    public List<Loan> userLoans(Long userId) {
        log.debug("Fetching loans for user ID: {}", userId);
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + userId));

        return loanRepository.findAllByUserId(userId);
    }

    @Override
    public Loan updateLoanStatus(Long id, LoanStatus status) {
        log.debug("Updating loan status for loan ID: {}", id);

        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID " + id));
        loan.setLoanStatus(status);

        //set remaining amount to zero if loan is accepted
        if(status == LoanStatus.ACCEPTED){
            loan.setRemainingAmount(loan.getCumulativeAmount());
        }

       return loanRepository.save(loan);
    }

    // TODO : schedule to run everyday at 9.am
    @Override
    @Transactional
    public void loanPaymentReminder(Long userId) {
        log.debug("About to send loan payment reminder to user ID: {}", userId);

        //check if user exists
        if(!userRepository.existsById(userId)){
            log.warn("User with ID {} does not exist",userId);
            throw new ResourceNotFoundException("User not found with ID"+userRepository);
        }

        List<Loan> unpaidLoans = loanRepository.findAllByUserId(userId)
                .stream()
                .filter(loan -> loan.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        log.debug("Unpaid loans for user with ID {} : {}",userId,unpaidLoans);

        unpaidLoans.forEach(unpaidLoan -> {
            String message = String.format("You have a pending loan of %s with due date %s", unpaidLoan.getRemainingAmount(), unpaidLoan.getEndDate());
            Notification notification = new Notification(
                    userId,
                    message,
                    LocalDateTime.now()
            );
            notificationService.createNotification(notification);

            log.debug(message);
        });


    }
}
