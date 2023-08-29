package com.cece.cards.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PagedCardResponse {
    private int pageNo;
    private int pageSize;
    private long totalItems;

    private List<CardResponse> data;
}
