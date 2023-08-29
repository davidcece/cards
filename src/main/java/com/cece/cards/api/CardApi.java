package com.cece.cards.api;


import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.dto.requests.CardRequest;
import com.cece.cards.dto.responses.CardResponse;
import com.cece.cards.dto.responses.PagedCardResponse;
import com.cece.cards.services.CardService;
import com.cece.cards.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/v1/cards")
@RequiredArgsConstructor
@CrossOrigin
public class CardApi {
    private final CardService cardService;
    private final UserService userService;

    @GetMapping
    public PagedCardResponse getCards(@RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> pageSize, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByEmail(username);
        return cardService.getCards(user, page, pageSize);
    }

    @PostMapping
    @Operation(summary = "Create a card",
            description = "Card name is required, description and color are optional," +
                    " if color is provided, it must be a valid hex color starting with #, must have either 4 characters eg #a1f" +
                    " or 7 characters eg #aa11ff")
    public CardResponse createCard(@Valid @RequestBody CardRequest request, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByEmail(username);

        Card postedCard = Card.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .user(user)
                .build();
        Card savedCard = cardService.create(postedCard);
        return savedCard.response();
    }



}
