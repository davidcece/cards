package com.cece.cards.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotNull
    @Email
    @Size.List({
            @Size(min = 5, message = "{validation.color.size.too_short}"),
            @Size(max = 50, message = "{validation.color.size.too_long}")
    })
    private String email;

    @NotNull
    @Size.List({
            @Size(min = 5, message = "{validation.color.size.too_short}"),
            @Size(max = 20, message = "{validation.color.size.too_long}")
    })
    private String password;

}