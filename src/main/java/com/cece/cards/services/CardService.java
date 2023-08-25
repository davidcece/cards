package com.cece.cards.services;

import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Card create(Card card) {
        return cardRepository.save(card);
    }

    public List<Card> getAllCards(int page, int pageSize) {
        return cardRepository.findAll();
    }

    public List<Card> getUserCards(int userId, int page, int pageSize) {
        return cardRepository.findAll();
    }
}
