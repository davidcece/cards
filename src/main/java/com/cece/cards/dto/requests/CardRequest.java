package com.cece.cards.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public class CardRequest {
    @NotNull(message = "validation.name.not_null")
    @NotEmpty(message = "validation.name.not_empty")
    private String name;

    private String description;

    @Size.List({
            @Size(min = 6, message = "{validation.color.size.too_short}"),
            @Size(max = 6, message = "{validation.color.size.too_long}")
    })
    private String color;
}
