package com.cece.cards.dto.requests;

import com.cece.cards.dto.requests.validations.CardStatus;
import com.cece.cards.dto.requests.validations.HexColor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UpdateCardRequest {

    @NotNull(message = "name should not be null")
    @NotEmpty(message = "name should not be empty")
    private String name;

    private String description;

    @HexColor
    private String color;

    @CardStatus
    private String status;
}
