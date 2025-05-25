package com.reciperestapi.reciperestapi.validators;

import com.reciperestapi.reciperestapi.recipe.repository.impl_service.RecipeDAOImpl;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RecipeNameExistsValidator implements ConstraintValidator<RecipeNameExistsValidatorConstraint, String> {
    @Inject
    private RecipeDAOImpl recipeDAO;
    @Override
    public void initialize(RecipeNameExistsValidatorConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String recipeName, ConstraintValidatorContext constraintValidatorContext) {
        return recipeDAO.findByName(recipeName) == null;
    }
}