package com.backend.banking_app.dto;

import com.backend.banking_app.model.enumerations.CardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDTO {


    private String cardNumber;

    private Long userId;

    private Long accountId;

    private CardType cardType;


}
