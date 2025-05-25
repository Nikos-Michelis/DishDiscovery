package com.reciperestapi.reciperestapi.recipe.repository;

import com.reciperestapi.reciperestapi.recipe.model.Recipe;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.user.model.User;

import java.util.List;

public interface GenericDAO<T> {
    T findById(Integer id);

    List<T> findAll();

    Object save(FormRecipeDTO recipe, User user);

    Object update(FormRecipeDTO recipe, User user);

    void delete(Integer id);

    Integer getTotalRows(PageRequest pageRequest, boolean isWishlist, User user);
}
