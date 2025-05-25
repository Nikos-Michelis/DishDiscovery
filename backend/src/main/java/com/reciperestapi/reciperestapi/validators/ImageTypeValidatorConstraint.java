package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageTypeValidatorConstraint {
    String message() default "Invalid image type. Supported types are: jpg, jpeg, png";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}