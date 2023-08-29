package com.cece.cards.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

}