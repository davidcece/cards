package com.cece.cards.dto.requests.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CardStatusValidator.class)
@Documented
public @interface CardStatus {

    String message() default "invalid status, accepted values: To Do, In Progress, Done";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}