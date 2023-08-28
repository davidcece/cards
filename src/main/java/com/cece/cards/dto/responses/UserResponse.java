package com.cece.cards.dto.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private int id;
    private String email;
}
