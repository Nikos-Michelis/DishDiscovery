package com.reciperestapi.reciperestapi.recipe.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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
public class CategoryDTO implements Serializable, DTOEntity {
    @JsonbProperty("id")
    @PositiveOrZero(message = "Invalid category type")
    private int categoryId;
    @JsonbProperty("name")
    @NotBlank(message = "Category Name must be not null or empty")
    private String categoryName;
}
