package com.cece.cards.dto.responses;

public enum ResponseCodes {
    SUCCESS(0, "Success"),
    INTERNAL_ERROR(500, "Internal server error"),
    NOT_FOUND(400, "Resource not found");

    private int code;
    private String description;

    ResponseCodes(int code, String description){
        this.code=code;
        this.description=description;
    }

    public int getCode(){
        return code;
    }
    public String getDescription(){
        return description;
    }
}
