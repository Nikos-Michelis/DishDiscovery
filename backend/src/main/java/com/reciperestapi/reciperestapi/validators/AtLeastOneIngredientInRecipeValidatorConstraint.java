package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneIngredientInRecipeValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneIngredientInRecipeValidatorConstraint {
    String message() default "You must add at least one ingredient.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}