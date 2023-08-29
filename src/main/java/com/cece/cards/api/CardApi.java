package com.cece.cards.api;


import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.dto.requests.CardRequest;
import com.cece.cards.dto.requests.SearchRequest;
import com.cece.cards.dto.requests.UpdateCardRequest;
import com.cece.cards.dto.responses.CardResponse;
import com.cece.cards.dto.responses.PagedCardResponse;
import com.cece.cards.services.CardService;
import com.cece.cards.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            description = "Card name is required <br />, description and color are optional, <br />" +
                    " if color is provided, it must be a valid hex color starting with #, must have either 4 characters eg #a1f" +
                    " or 7 characters eg #aa11ff")
    public CardResponse createCard(@Valid @RequestBody CardRequest request, Principal principal) {
        Card card = cardService.create(request, getUser(principal));
        return card.response();
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a card",
            description = "Card name is required <br />, description, color and status are optional,<br />" +
                    " if color is provided, it must be a valid hex color starting with #, must have either 4 characters eg #a1f" +
                    " or 7 characters eg #aa11ff, <br />" +
                    " status is one [To Do, In Progress, Done]")
    public ResponseEntity<CardResponse> updateCard(@PathVariable long id, @Valid @RequestBody UpdateCardRequest request, Principal principal) {
        ResponseEntity<CardResponse> UNAUTHORIZED = validateUserAccess(id, principal);
        if (UNAUTHORIZED != null) return UNAUTHORIZED;

        Card updatedCard = cardService.update(id, request);
        return ResponseEntity.ok().body(updatedCard.response());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CardResponse> delete(@PathVariable long id, Principal principal) {
        ResponseEntity<CardResponse> UNAUTHORIZED = validateUserAccess(id, principal);
        if (UNAUTHORIZED != null) return UNAUTHORIZED;

        cardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<CardResponse> getCard(@PathVariable long id, Principal principal) {
        ResponseEntity<CardResponse> UNAUTHORIZED = validateUserAccess(id, principal);
        if (UNAUTHORIZED != null) return UNAUTHORIZED;

        Card card = cardService.getCard(id);
        return ResponseEntity.ok().body(card.response());
    }


    @GetMapping("/search")
    @Operation(summary = "Search cards",
            description = "fromDate and toDate are of format yyyy-MM-dd <br />" +
                    "sortBy can either be [name, color, status, createdAt] if not provided the default is createdAt <br />" +
                    "sortDescending boolean true or false, default is false")
    public PagedCardResponse search(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> color,
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> fromDate,
            @RequestParam Optional<String> toDate,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Boolean> sortDescending,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> pageSize,
            Principal principal) {

        String username = principal.getName();
        User user = userService.getUserByEmail(username);

        SearchRequest request=SearchRequest.builder()
                .name(name)
                .color(color)
                .status(status)
                .fromDate(fromDate)
                .toDate(toDate)
                .sortBy(sortBy)
                .sortDescending(sortDescending)
                .page(page)
                .pageSize(pageSize)
                .user(user)
                .build();

        return cardService.searchCards(request);
    }


    private ResponseEntity<CardResponse> validateUserAccess(long cardId, Principal principal) {
        User user = getUser(principal);
        Card oldCard = cardService.getCard(cardId);
        if (!userCanAccessCard(user, oldCard))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return null;
    }

    private User getUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    private static boolean userCanAccessCard(User user, Card card) {
        return user.isAdmin() || card.getUser().getId() == user.getId();
    }


}
