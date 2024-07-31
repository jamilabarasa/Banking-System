package com.backend.banking_app.repository;

import com.backend.banking_app.model.Account;
import com.backend.banking_app.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    List<Account> findAllByuserId(Long id);
}
