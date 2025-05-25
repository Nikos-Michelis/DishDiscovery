package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = RecipeIdExistsValidator.class)
public @interface RecipeIdExistsValidatorConstraint {
    String message() default "invalid id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
