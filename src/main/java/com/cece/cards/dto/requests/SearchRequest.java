package com.cece.cards.dto.requests;

import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import lombok.Builder;
import lombok.Getter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Builder
@Getter
public class SearchRequest {
    private Optional<String> name;
    private Optional<String> color;
    private Optional<String> status;
    private Optional<String> fromDate;
    private Optional<String> toDate;
    private Optional<String> sortBy;
    private Optional<Boolean> sortDescending;
    private Optional<Integer> page;
    private Optional<Integer> pageSize;
    private User user;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Card> filterCards(List<Card> cards, int defaultPageNo, int defaultPageSize) {
        long skip = getItemsToSkip(defaultPageNo, defaultPageSize);
        cards = cards.parallelStream()
                .filter(byName())
                .filter(byColor())
                .filter(byStatus())
                .filter(byStartDate())
                .filter(byEndDate())
                .sorted(getComparator())
                .skip(skip)
                .toList();
        int take = pageSize.orElse(defaultPageSize);
        return cards.stream().limit(take).toList();
    }

    private long getItemsToSkip(int defaultPageNo, int defaultPageSize) {
        int number = page.orElse(defaultPageNo);
        int size = pageSize.orElse(defaultPageSize);
        return (long) number * size;
    }

    private Predicate<Card> byName() {
        return card -> name.isEmpty() || name.get().equals(card.getName());
    }

    private Predicate<Card> byColor() {
        return card -> {
            if (color.isEmpty())
                return true;
            String decodedColor = URLDecoder.decode(color.get(), StandardCharsets.UTF_8);
            return decodedColor.equals(card.getColor());
        };
    }

    private Predicate<Card> byStatus() {
        return card -> status.isEmpty() || status.get().equals(card.getStatus());
    }

    private Predicate<Card> byStartDate() {
        LocalDateTime startDate;
        if (fromDate.isPresent())
            startDate = LocalDate.parse(fromDate.get(), formatter).atStartOfDay();
        else
            startDate = null;
        return card -> startDate == null || startDate.isBefore(card.getCreatedAt());
    }

    private Predicate<Card> byEndDate() {
        LocalDateTime endDate;
        if (toDate.isPresent())
            endDate = LocalDate.parse(toDate.get(), formatter).atStartOfDay().plusDays(1);
        else
            endDate = null;
        return card -> endDate == null || endDate.isAfter(card.getCreatedAt());
    }


    private Comparator<Card> getComparator() {
        String column = sortBy.orElse("createdAt");
        Comparator<Card> comparator = switch (column) {
            case "createdAt" -> Comparator.comparing(Card::getCreatedAt);
            case "name" -> Comparator.comparing(Card::getName);
            case "color" -> Comparator.comparing(Card::getColor);
            case "status" -> Comparator.comparing(Card::getStatus);
            default -> throw new IllegalStateException("Unexpected sortBy value: " + column);
        };

        if (sortDescending.isPresent() && Boolean.TRUE.equals(sortDescending.get())) {
            return comparator.reversed();
        }
        return comparator;
    }
}
