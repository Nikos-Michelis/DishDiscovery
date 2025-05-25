package com.reciperestapi.reciperestapi.recipe.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient implements Serializable {
    private int recipeId;
    private int ingredientId;
    private String ingredientName;
    private String ingredientAmount;
    private Measurement measurement;
    private IngredientCategory ingredientCategory;
    private String ingredientCategoryName;
}
