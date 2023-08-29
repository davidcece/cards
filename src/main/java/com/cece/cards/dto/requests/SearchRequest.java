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
        LocalDateTime startDate = getStartDate();
        LocalDateTime endDate = getEndDate();
        long skip = getSkip(defaultPageNo, defaultPageSize);

        cards = cards.parallelStream()
                .filter(c -> name.isEmpty() || name.get().equals(c.getName()))
                .filter(c -> color.isEmpty() || decodeColor().equals(c.getColor()))
                .filter(c -> status.isEmpty() || status.get().equals(c.getStatus()))
                .filter(c -> startDate == null || startDate.isBefore(c.getCreatedAt()))
                .filter(c -> endDate == null || endDate.isAfter(c.getCreatedAt()))
                .sorted(getComparator())
                .skip(skip).toList();

        //Do limit after filtering and sorting
        long take = getLimitOrDefault(defaultPageSize);
        return cards.stream().limit(take).toList();
    }

    private String decodeColor() {
        String cardColor = color.get();
        return URLDecoder.decode(cardColor, StandardCharsets.UTF_8);
    }

    private long getSkip(int defaultPageNo, int defaultPageSize) {
        if (page.isPresent() && pageSize.isPresent()) {
            return (long) page.get() * pageSize.get();
        }
        return (long) defaultPageNo * defaultPageSize;
    }

    private long getLimitOrDefault(int defaultPageSize) {
        if (pageSize.isPresent())
            return pageSize.get();
        return defaultPageSize;
    }

    private Comparator<Card> getComparator() {
        String column = sortBy.orElse("createdAt");
        Comparator<Card> comparator = switch (column) {
            case "createdAt" -> Comparator.comparing(Card::getCreatedAt);
            case "name" -> Comparator.comparing(Card::getName);
            case "color" -> Comparator.comparing(Card::getColor);
            case "status" -> Comparator.comparing(Card::getStatus);
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };

        if (sortDescending.isPresent() && Boolean.TRUE.equals(sortDescending.get())) {
            return comparator.reversed();
        }
        return comparator;
    }


    private LocalDateTime getStartDate() {
        if (fromDate.isPresent()) {
            LocalDate localDate = LocalDate.parse(fromDate.get(), formatter);
            return localDate.atStartOfDay();
        }
        return null;
    }

    private LocalDateTime getEndDate() {
        if (toDate.isPresent()) {
            LocalDate localDate = LocalDate.parse(toDate.get(), formatter);
            return localDate.atStartOfDay().plusDays(1);
        }
        return null;
    }


}
