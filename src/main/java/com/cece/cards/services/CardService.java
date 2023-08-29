package com.cece.cards.services;

import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.datalayer.repositories.CardRepository;
import com.cece.cards.dto.responses.CardResponse;
import com.cece.cards.dto.responses.PagedCardResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    @Value("${app.default.page-no}")
    private int defaultPageNo;

    @Value("${app.default.page-size}")
    private int defaultPageSize;

    private final CardRepository cardRepository;

    public Card create(Card card) {
        return cardRepository.save(card);
    }

    public long countAllCards(User user) {
        if (user.isAdmin())
            return cardRepository.count();
        return cardRepository.countByUserId(user.getId());
    }

    public PagedCardResponse getCards(User user, Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        Pageable pageable = getPageable(pageNumber, pageSize);
        long count = countAllCards(user);
        if (user.isAdmin()) {
            List<Card> cards = cardRepository.findAll(pageable).toList();
            return responseFrom(cards, pageable, count);
        } else {
            List<Card> cards = cardRepository.findAllByUserId(user.getId(), pageable);
            return responseFrom(cards, pageable, count);
        }
    }

    private Pageable getPageable(Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        int page = pageNumber.orElse(defaultPageNo);
        int size = pageSize.orElse(defaultPageSize);
        return PageRequest.of(page, size, Sort.by("id"));
    }


    private PagedCardResponse responseFrom(List<Card> cards, Pageable pageable, long count) {
        List<CardResponse> cardResponses = cards.stream().map(Card::response).toList();
        return PagedCardResponse.builder()
                .totalItems(count)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .data(cardResponses)
                .build();
    }

}
