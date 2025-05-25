package com.reciperestapi.reciperestapi.recipe.dto;
import com.reciperestapi.reciperestapi.user.dto.UserDTO;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"id", "name", "category", "difficulty", "executionTime", "image", "ingredients", "steps", "author", "isWishlisted"})
public class RecipeDTO implements Serializable, DTOEntity {
    @JsonbProperty("id")
    private Integer recipeId;
    @JsonbProperty("name")
    private String recipeName;
    @JsonbProperty("category")
    private String category;
    private String difficulty;
    private Integer executionTime;
    private String image;
    @JsonbProperty("ingredients")
    private List<IngredientDTO> ingredientsList;
    @JsonbProperty("steps")
    private List<RecipeStepsDTO> stepsList;
    @JsonbProperty("author")
    private UserDTO author;
    private boolean isWishlisted;
}
