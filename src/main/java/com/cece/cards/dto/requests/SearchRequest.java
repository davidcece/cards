package com.cece.cards.dto.requests;

import com.cece.cards.datalayer.models.Card;
import com.cece.cards.datalayer.models.User;
import lombok.Builder;
import lombok.Getter;

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

        cards = cards.stream().filter(c -> name.isEmpty() || name.get().equals(c.getName())).toList();
        cards = cards.stream().filter(c -> color.isEmpty() || color.get().equals(c.getColor())).toList();
        cards = cards.stream().filter(c -> status.isEmpty() || status.get().equals(c.getStatus())).toList();
        cards = cards.stream().filter(c -> startDate == null || startDate.isBefore(c.getCreatedAt())).toList();
        cards = cards.stream().filter(c -> endDate == null || endDate.isAfter(c.getCreatedAt())).toList();
        cards = cards.stream().sorted(getComparator()).toList();
        cards = cards.stream().skip(skip).toList();

        //Do limit after filtering and sorting
        long take = getLimitOrDefault(defaultPageSize);
        return cards.stream().limit(take).toList();
    }

    private long getSkip(int defaultPageNo, int defaultPageSize) {
        if (page.isPresent() && pageSize.isPresent()) {
            return page.get() * pageSize.get();
        }
        return defaultPageNo * defaultPageSize;
    }

    private long getLimitOrDefault(int defaultPageSize) {
        if (pageSize.isPresent())
            return pageSize.get();
        return defaultPageSize;
    }

    private Comparator<Card> getComparator() {
        Comparator<Card> comparator;
        if (sortBy.isEmpty())
            comparator = Comparator.comparing(Card::getCreatedAt);
        else {
            comparator = switch (sortBy.get()) {
                case "createdAt" -> Comparator.comparing(Card::getCreatedAt);
                case "name" -> Comparator.comparing(Card::getName);
                case "color" -> Comparator.comparing(Card::getColor);
                case "status" -> Comparator.comparing(Card::getStatus);
                default -> throw new IllegalStateException("Unexpected value: " + sortBy.get());
            };
        }

        if (sortDescending.isPresent() && sortDescending.get()) {
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
