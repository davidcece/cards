package com.cece.cards.api;


import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.dto.requests.CardRequest;
import com.cece.cards.dto.responses.CardResponse;
import com.cece.cards.services.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cards")
@RequiredArgsConstructor
public class CardApi {
    private final CardService cardService;

    @GetMapping
    public List<CardResponse> getCards(@RequestParam int page, @RequestParam int pageSize) {
        User user = getUser();
        List<Card> cards;
        if (user.isAdmin())
            cards = cardService.getAllCards(page, pageSize);
        else
            cards = cardService.getUserCards(user.getId(), page, pageSize);
        return responseFrom(cards);
    }

    @PostMapping
    public CardResponse createCard(@Valid @RequestBody CardRequest request) {
        Card postedCard = Card.builder()

                .build();
        Card savedCard = cardService.create(postedCard);
        return savedCard.response();
    }

    private List<CardResponse> responseFrom(List<Card> cards) {
        return cards.stream().map(Card::response).toList();
    }

    private User getUser() {
        return new User();
    }
}
