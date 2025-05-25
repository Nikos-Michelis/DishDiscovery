package com.reciperestapi.reciperestapi.recipe.model;

import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientCategory implements Serializable, DTOEntity {
    private int ingredientCategoryId;
    private String ingredientCategory;
}


