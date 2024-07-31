package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.CardDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.mapper.CardMapper;
import com.backend.banking_app.model.Card;
import com.backend.banking_app.model.enumerations.CardAction;
import com.backend.banking_app.repository.CardRepository;
import com.backend.banking_app.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public Card getCardById(Long id) {
        log.debug("About to get card by ID {}",id);

        Card card = cardRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Card not found with ID "+id));
        return card;
    }

    @Override
    public Card getCardByNumber(String cardNumber) {
        log.debug("About to get card by cardNumber {}",cardNumber);

        Card card = cardRepository.findByCardNumber(cardNumber).orElseThrow(()->
                new ResourceNotFoundException("Card not found with cardNumber "+cardNumber));

        return card;
    }

    @Override
    public Card createCard(CardDTO cardDTO) {
        log.debug("About to save card with account number {}",cardDTO.getAccountId());
        Card card = CardMapper.maptoCard(cardDTO);
        return cardRepository.save(card);
    }

    @Override
    public Card updateCardStatus(Long cardId, CardAction action) {
        log.debug("About to update card with ID {}",cardId);
        //check if card exists
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new ResourceNotFoundException("Card not found with ID "+cardId));


        // Update card status
        if (action == CardAction.ACTIVATE) {
            card.setActive(true);
            log.debug("Card with ID {} activated", cardId);
        } else if (action == CardAction.DEACTIVATE) {
            card.setActive(false);
            log.debug("Card with ID {} deactivated", cardId);
        } else {
            log.warn("Unknown CardAction: {}", action);
            throw new BadRequestException("Unknown Card Action");
        }

        return cardRepository.save(card);
    }

    @Override
    public List<Card> getUserCards(Long userId) {
        log.debug("About to get cards for a user with ID  {}",userId);

        List<Card> cards = cardRepository.findAllByUserId(userId);

        return cards;
    }


}
