package com.reciperestapi.reciperestapi.recipe.repository;

import java.util.List;

public interface CommonDAO<T> {
    List<T> findAllRecipeCategory();
    List<T> findAllIngredientCategory();
    List<T> findAllIngredients();
    List<T> findAllMeasurements();
}
