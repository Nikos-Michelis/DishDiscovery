package com.reciperestapi.reciperestapi.recipe.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"step", "instruction"})
public class RecipeStepsDTO implements Serializable ,DTOEntity {
    @JsonbProperty("instruction")
    @NotBlank(message = "Step instruction must be not null or empty")
    private String stepInstructions;
    @JsonbProperty("step")
    @PositiveOrZero(message = "Invalid step number")
    private Integer stepNumber;
}

