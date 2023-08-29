package com.cece.cards.services;

import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.datalayer.repositories.CardRepository;
import com.cece.cards.dto.requests.CardRequest;
import com.cece.cards.dto.requests.SearchRequest;
import com.cece.cards.dto.requests.UpdateCardRequest;
import com.cece.cards.dto.responses.CardResponse;
import com.cece.cards.dto.responses.PagedCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Card getCard(long id) {
        return cardRepository.findOneById(id);
    }

    public Card create(CardRequest request, User user) {
        Card card = Card.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status("To Do")
                .build();
        return cardRepository.save(card);
    }

    public Card update(long id, UpdateCardRequest request) {
        Card card = getCard(id);

        card.setName(request.getName());
        card.setDescription(request.getDescription());
        card.setColor(request.getColor());
        card.setUpdatedAt(LocalDateTime.now());
        if (available(request.getStatus()))
            card.setStatus(request.getStatus());

        return cardRepository.save(card);
    }

    public void delete(long id) {
        Card card = getCard(id);
        cardRepository.delete(card);
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

    public PagedCardResponse searchCards(SearchRequest request) {
        User user = request.getUser();
        List<Card> cards;
        if (user.isAdmin()) {
            cards = cardRepository.findAll();
        } else {
            cards = cardRepository.findAllByUserId(user.getId());
        }

        cards = request.filterCards(cards, defaultPageNo, defaultPageSize);

        long count = cards.size();
        Pageable pageable = getPageable(request.getPage(), request.getPageSize());
        return responseFrom(cards, pageable, count);
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

    private boolean available(String item) {
        return item != null && !item.trim().equals("");
    }


}
