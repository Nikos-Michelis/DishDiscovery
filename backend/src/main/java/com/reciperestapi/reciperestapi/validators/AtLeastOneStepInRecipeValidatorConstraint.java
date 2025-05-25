package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneStepInRecipeValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneStepInRecipeValidatorConstraint {
    String message() default "You must add at least one step.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}