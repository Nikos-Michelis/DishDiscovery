package com.reciperestapi.reciperestapi.common.services;

import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

@ApplicationScoped
public class ValidationService {
    @Inject
    private ValidatorFactory validatorFactory;
    public void handleViolations(FormRecipeDTO formRecipeDTO) {
        Set<ConstraintViolation<FormRecipeDTO>> violations = validatorFactory.getValidator().validate(formRecipeDTO);
        if (!violations.isEmpty()){
            System.out.println("- errors: " + violations);
            throw new ConstraintViolationException(violations);
        }
    }
}
