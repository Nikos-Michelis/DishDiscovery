package com.reciperestapi.reciperestapi.validators;

import com.reciperestapi.reciperestapi.recipe.dto.forms.FormIngredientDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class IngredientValidator implements ConstraintValidator<IngredientValidatorConstraint, FormIngredientDTO> {

    @Override
    public void initialize(IngredientValidatorConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(FormIngredientDTO formIngredientDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (formIngredientDTO.getIngredientId() != null && formIngredientDTO.getIngredientId() > 0
                && (formIngredientDTO.getIngredientName() != null && !formIngredientDTO.getIngredientName().isEmpty())) {
            return false;
        }

        if ((formIngredientDTO.getIngredientId() == null || formIngredientDTO.getIngredientId() == 0)
                && (formIngredientDTO.getIngredientName() == null || formIngredientDTO.getIngredientName().isEmpty())) {
            return false;
        }

        if (formIngredientDTO.getIngredientName() != null) {
            // Define your regex pattern
            String regexPattern = "^[a-zA-Z]+$"; // Modify this pattern according to your requirements

            // Check if the ingredientName matches the regex pattern
            if (!formIngredientDTO.getIngredientName().matches(regexPattern)) {
                // Set custom error message
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid ingredient name.").addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}