package com.cece.cards.dto.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardResponse {
    private long id;
    private String name;
    private String description;
    private String color;
    private String status;
    private int userId;
}
