package com.cece.cards.dto.responses;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CardResponse {
    private long id;
    private String name;
    private String description;
    private String color;
    private String status;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
