package com.backend.banking_app.repository;

import com.backend.banking_app.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserId(Long userId);

    Optional<Card> findByCardNumber(String cardNumber);
}


