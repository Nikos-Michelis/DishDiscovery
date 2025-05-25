package com.reciperestapi.reciperestapi.recipe.repository;

import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.user.model.User;

import java.util.List;

public interface RecipeDAO<T> {
    List<T> findByCriteria(PageRequest pageRequest, boolean isWishlist, User user);
    T findByName(String name);
    T findWishlistByUserId(User user);
    List<T> findAllWeeklyRecipes();

}
