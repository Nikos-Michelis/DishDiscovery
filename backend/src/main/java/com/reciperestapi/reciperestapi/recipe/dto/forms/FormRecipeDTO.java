package com.reciperestapi.reciperestapi.recipe.dto.forms;

import com.reciperestapi.reciperestapi.recipe.dto.ImageInfoDTO;
import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import com.reciperestapi.reciperestapi.recipe.dto.RecipeStepsDTO;
import com.reciperestapi.reciperestapi.recipe.model.Difficulty;
import com.reciperestapi.reciperestapi.validators.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class FormRecipeDTO implements Serializable, DTOEntity {
    @RecipeIdExistsValidatorConstraint(message = "Recipe does not exist.")
    @Positive(message = "Invalid recipe identifier.")
    private Integer recipeId;
    @NotBlank(message = "Name must not be null or empty.")
    private String recipeName;
    @PositiveOrZero(message = "Invalid category id.")
    @NotNull(message = "Difficulty must not be null.")
    private Integer categoryId;
    @NotBlank(message = "Difficulty must not be null or empty.")
    @ValueOfEnumValidatorConstraint(enumClass = Difficulty.class, message = "The Difficulty must be one of the following values: Easy, Normal, Hard.")
    private String difficulty;
    @NotNull(message = "Execution time must not be null.")
    @Min(value = 1, message = "Execution time must be greater to 0.")
    private Integer executionTime;
    @AtLeastOneIngredientInRecipeValidatorConstraint
    private List<@Valid FormIngredientDTO> ingredients;
    private List<@Valid ImageInfoDTO> image;
    @AtLeastOneStepInRecipeValidatorConstraint
    private List<@Valid RecipeStepsDTO> steps;
}
