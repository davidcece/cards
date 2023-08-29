package com.cece.cards.dto.requests.validations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HexColorValidator.class)
@Documented
public @interface HexColor {

    String message() default "Invalid hex color; length must be 4 or 7 starting with # symbol," +
            " and only contain these characters 0123456789abcdef";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
