package com.reciperestapi.reciperestapi.validators;
import com.reciperestapi.reciperestapi.recipe.dto.RecipeStepsDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

@ApplicationScoped
public class AtLeastOneStepInRecipeValidator implements ConstraintValidator<AtLeastOneStepInRecipeValidatorConstraint, List<RecipeStepsDTO>> {
    @Override
    public void initialize(AtLeastOneStepInRecipeValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(List<RecipeStepsDTO> recipesStepsList, ConstraintValidatorContext constraintValidatorContext) {
        return recipesStepsList
                .stream()
                .filter(recipeStepsDTO -> recipeStepsDTO.getStepInstructions() !=null)
                .filter(recipeStepsDTO -> recipeStepsDTO.getStepNumber() > 0)
                .count() > 0;
    }
}
