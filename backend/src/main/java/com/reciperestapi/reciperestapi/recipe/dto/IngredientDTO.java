package com.reciperestapi.reciperestapi.recipe.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonbPropertyOrder({"id", "name", "amount", "measurement", "category"})
public class IngredientDTO implements Serializable, DTOEntity {
    @JsonbProperty("id")
    private int ingredientId;
    @JsonbProperty("name")
    private String ingredientName;
    @JsonbProperty("amount")
    private String ingredientAmount;
    @JsonbProperty("measurement")
    private MeasurementsDTO measurement;
    @JsonbProperty("category")
    private IngredientCategoryDTO ingredientCategory;
}