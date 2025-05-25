package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = {IngredientValidator.class})
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IngredientValidatorConstraint {
    String message() default "Invalid Ingredient select or add new one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}