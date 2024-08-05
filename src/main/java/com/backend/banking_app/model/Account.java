package com.backend.banking_app.model;

import com.backend.banking_app.model.enumerations.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account Number is required")
    @Column(unique = true,nullable = false)
    private String accountNumber;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @NotNull(message = "AccountType is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    private BigDecimal balance = BigDecimal.ZERO; // Default value of 0;


}
