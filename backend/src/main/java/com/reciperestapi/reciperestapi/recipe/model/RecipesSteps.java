package com.reciperestapi.reciperestapi.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipesSteps implements Serializable {
    private int recipeId;
    private String stepInstructions;
    private int stepNumber;
}
