package com.backend.banking_app.controller;

import com.backend.banking_app.dto.CardDTO;
import com.backend.banking_app.model.Card;
import com.backend.banking_app.model.enumerations.CardAction;
import com.backend.banking_app.service.CardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/cards")
public class CardController {
    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardDTO cardDTO) {
        log.debug("REST Request to create card for account number {}", cardDTO.getAccountId());
        Card card = cardService.createCard(cardDTO);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        log.debug("REST Request to get card with ID {}", id);
        Card card = cardService.getCardById(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/cardNumber/{cardNumber}")
    public ResponseEntity<Card> getCardByAccountId(@PathVariable String cardNumber) {
        log.debug("REST Request to get card with Card Number {}", cardNumber);
        Card card = cardService.getCardByNumber(cardNumber);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Card>> getUserCards(@PathVariable Long userId) {
        log.debug("REST Request to get all cards for user with ID", userId);
        List<Card> cards = cardService.getUserCards(userId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateCardStatus(@PathVariable Long cardId, @RequestParam CardAction cardAction) {
        log.debug("REST Request to update card with id {}", cardId);
        Card card = cardService.updateCardStatus(cardId, cardAction);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

}

