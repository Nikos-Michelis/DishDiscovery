package com.reciperestapi.reciperestapi.recipe.dto.forms;

import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import com.reciperestapi.reciperestapi.validators.IngredientValidatorConstraint;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
@IngredientValidatorConstraint
public class FormIngredientDTO implements Serializable, DTOEntity {
    private int recipeId;
    @PositiveOrZero(message = "Invalid ingredient id")
    private Integer ingredientId;
    private String ingredientName;
    @NotBlank(message = "Ingredient amount must be not null or empty")
    private String ingredientAmount;
    @PositiveOrZero(message = "Invalid unit id")
    private Integer unitId;
    @PositiveOrZero(message = "Invalid ingredient category id")
    private Integer ingredientCategoryId;
}

