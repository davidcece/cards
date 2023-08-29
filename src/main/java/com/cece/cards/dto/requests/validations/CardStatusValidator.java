package com.cece.cards.dto.requests.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CardStatusValidator implements ConstraintValidator<CardStatus, String> {

    @Override
    public void initialize(CardStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        List<String> validStatuses=List.of("To Do", "In Progress", "Done");
        return validStatuses.contains(status);
    }


}
