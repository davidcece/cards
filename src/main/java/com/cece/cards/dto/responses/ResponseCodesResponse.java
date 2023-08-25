package com.cece.cards.dto.responses;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseCodesResponse {
    private int code;
    private String description;
}
