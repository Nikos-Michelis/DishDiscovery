package com.reciperestapi.reciperestapi.validators;

import com.reciperestapi.reciperestapi.recipe.repository.impl_service.RecipeDAOImpl;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RecipeIdExistsValidator implements ConstraintValidator<RecipeIdExistsValidatorConstraint, Integer> {
    @Inject
    private RecipeDAOImpl recipeDAO;
    @Override
    public void initialize(RecipeIdExistsValidatorConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer recipe_id, ConstraintValidatorContext constraintValidatorContext) {
        if (recipe_id == null) {
            return true;
        }
        return recipeDAO.findById(recipe_id) != null;
    }
}
