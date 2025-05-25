package com.reciperestapi.reciperestapi.validators;

import com.reciperestapi.reciperestapi.recipe.dto.forms.FormIngredientDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

@ApplicationScoped
public class AtLeastOneIngredientInRecipeValidator implements ConstraintValidator<AtLeastOneIngredientInRecipeValidatorConstraint, List<FormIngredientDTO>> {
    @Override
    public void initialize(AtLeastOneIngredientInRecipeValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(List<FormIngredientDTO> ingredientList, ConstraintValidatorContext constraintValidatorContext) {
        return ingredientList.stream()
                .filter(ingredient -> ingredient.getIngredientAmount() != null)
                .count() > 0;
    }
}
