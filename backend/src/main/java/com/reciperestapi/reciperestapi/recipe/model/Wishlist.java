package com.reciperestapi.reciperestapi.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wishlist {
    private int wishlistId;
    private int userId;
    private int recipeId;
}
