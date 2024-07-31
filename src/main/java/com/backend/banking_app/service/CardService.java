package com.backend.banking_app.service;

import com.backend.banking_app.dto.CardDTO;
import com.backend.banking_app.model.Card;
import com.backend.banking_app.model.enumerations.CardAction;

import java.util.List;

public interface CardService {

    Card getCardById(Long id);
    Card getCardByNumber(String cardNumber);
    Card createCard(CardDTO cardDTO);;
    Card updateCardStatus(Long cardId, CardAction action);
    List<Card> getUserCards(Long userId);


}
