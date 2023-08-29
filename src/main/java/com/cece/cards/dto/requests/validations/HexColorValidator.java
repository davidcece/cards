package com.cece.cards.dto.requests.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HexColorValidator implements ConstraintValidator<HexColor, String> {

    @Override
    public void initialize(HexColor constraintAnnotation) {
    }

    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        if(color==null || color.trim().length()==0)
            return true;

        color=color.trim();
        if (color.length() != 4 && color.length() != 7) {
            return false;
        }

        if(color.charAt(0)!='#')
            return false;

        String allowedAlphaNumeric = "#0123456789abcdef";
        for (char c : color.toCharArray()) {
            String currentChar = String.valueOf(c).toLowerCase();
            if (!allowedAlphaNumeric.contains(currentChar)) {
                return false;
            }
        }

        return true;
    }


}
