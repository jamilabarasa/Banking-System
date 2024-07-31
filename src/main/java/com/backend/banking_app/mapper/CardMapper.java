package com.backend.banking_app.mapper;

import com.backend.banking_app.dto.CardDTO;
import com.backend.banking_app.model.Card;

public class CardMapper {

    public static Card maptoCard(CardDTO cardDTO){
        Card card = new Card(
                cardDTO.getCardNumber(),
                cardDTO.getUserId(),
                cardDTO.getAccountId(),
                cardDTO.getCardType()

        );

        return card;
    }


}
