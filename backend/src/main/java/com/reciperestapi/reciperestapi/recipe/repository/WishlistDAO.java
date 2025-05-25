package com.reciperestapi.reciperestapi.recipe.repository;

import com.reciperestapi.reciperestapi.recipe.model.Recipe;
import com.reciperestapi.reciperestapi.recipe.model.Wishlist;
import com.reciperestapi.reciperestapi.user.model.User;

import java.util.List;
import java.util.Optional;

public interface WishlistDAO {

    List<Wishlist> findWishlistByUserId(User user);
    void issueWishlist(User user);
    void addToWishlist(User user, Integer recipeId);
}

