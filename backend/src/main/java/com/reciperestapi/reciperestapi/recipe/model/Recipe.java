package com.reciperestapi.reciperestapi.recipe.model;
import com.reciperestapi.reciperestapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe implements Serializable {
    private int recipeId;
    private String recipeName;
    private Category category;
    private String difficulty;
    private int executionTime;
    private String image;
    private List<Ingredient> ingredientsList;
    private List<RecipesSteps> stepsList;
    private User author;
    private int wishListId;
    private int userId;
    private boolean isWishlisted;
}
