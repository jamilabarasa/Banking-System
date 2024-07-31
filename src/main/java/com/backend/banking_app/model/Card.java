package com.backend.banking_app.model;

import com.backend.banking_app.model.enumerations.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Card Type is required")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private boolean isActive;

    public Card(String cardNumber, Long userId, Long accountId, CardType cardType) {
        this.cardNumber = cardNumber;
        this.userId = userId;
        this.accountId = accountId;
        this.cardType = cardType;
    }


}
