package com.reciperestapi.reciperestapi.recipe.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"id", "name"})
public class IngredientCategoryDTO implements Serializable, DTOEntity {
    @JsonbProperty("id")
    private int ingredientCategoryId;
    @JsonbProperty("name")
    private String ingredientCategory;
}
